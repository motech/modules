package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CaseJson;
import org.motechproject.commcare.domain.CaseResponseJson;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.exception.CaseParserException;
import org.motechproject.commcare.gateway.CaseTaskXmlConverter;
import org.motechproject.commcare.request.json.CaseRequest;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CommcareCaseServiceImpl implements CommcareCaseService {

    @Autowired
    private CaseTaskXmlConverter converter;

    private MotechJsonReader motechJsonReader;

    private CommCareAPIHttpClient commcareHttpClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CommcareCaseServiceImpl(CommCareAPIHttpClient commcareHttpClient) {
        this.commcareHttpClient = commcareHttpClient;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public CaseInfo getCaseByCaseIdAndUserId(String caseId, String userId) {
        CaseRequest request = new CaseRequest();
        request.setCaseId(caseId);
        request.setUserId(userId);
        List<CaseJson> caseResponses = getCaseResponse(request).getCases();
        List<CaseInfo> cases = generateCasesFromCaseResponse(caseResponses);
        if (cases.size() == 0) {
            return null;
        }
        return cases.get(0);
    }

    @Override
    public CaseInfo getCaseByCaseId(String caseId) {
        String response = commcareHttpClient.singleCaseRequest(caseId);

        CaseJson caseResponses = parseSingleCaseFromResponse(response);

        return generateCaseFromCaseResponse(caseResponses);
    }

    @Override
    public List<CaseInfo> getCasesByType(String type, Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setType(type);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        List<CaseJson> caseResponses = getCaseResponse(request).getCases();
        return generateCasesFromCaseResponse(caseResponses);
    }

    @Override
    public List<CaseInfo> getCasesByUserId(String userId, Integer pageSize, Integer pageNumber) { //ONLY TEST
        CaseRequest request = new CaseRequest();
        request.setUserId(userId);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        List<CaseJson> caseResponses = getCaseResponse(request).getCases();
        return generateCasesFromCaseResponse(caseResponses);
    }

    @Override
    public CasesInfo getCasesByCasesNameWithMetadata(String caseName, Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setCaseName(caseName);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        CaseResponseJson caseResponseJson = getCaseResponse(request);

        return new CasesInfo(generateCasesFromCaseResponse(getCaseResponse(request).getCases()),
                populateCaseMetadata(caseResponseJson.getMetadata()));
    }

    @Override
    public CasesInfo getCasesByCasesTimeWithMetadata(String dateModifiedStart, String dateModifiedEnd, Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setDateModifiedStart(dateModifiedStart);
        request.setDateModifiedEnd(dateModifiedEnd);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        CaseResponseJson caseResponseJson = getCaseResponse(request);

        return new CasesInfo(generateCasesFromCaseResponse(getCaseResponse(request).getCases()),
                populateCaseMetadata(caseResponseJson.getMetadata()));
    }

    @Override
    public CasesInfo getCasesByCasesNameAndTimeWithMetadata(String caseName, String dateModifiedStart, String dateModifiedEnd, Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setCaseName(caseName);
        request.setDateModifiedStart(dateModifiedStart);
        request.setDateModifiedEnd(dateModifiedEnd);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        CaseResponseJson caseResponseJson = getCaseResponse(request);

        return new CasesInfo(generateCasesFromCaseResponse(getCaseResponse(request).getCases()),
                populateCaseMetadata(caseResponseJson.getMetadata()));
    }

    @Override
    public List<CaseInfo> getCasesByUserIdAndType(String userId, String type, Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setUserId(userId);
        request.setType(type);
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);
        List<CaseJson> caseResponses = getCaseResponse(request).getCases();
        return generateCasesFromCaseResponse(caseResponses);
    }

    @Override
    public List<CaseInfo> getCases(Integer pageSize, Integer pageNumber) {
        CaseRequest request = prepareCaseRequest(pageSize, pageNumber);
        return generateCasesFromCaseResponse(getCaseResponse(request).getCases());
    }

    @Override
    public CasesInfo getCasesWithMetadata(Integer pageSize, Integer pageNumber) {
        CaseRequest request = prepareCaseRequest(pageSize, pageNumber);
        CaseResponseJson caseResponseJson = getCaseResponse(request);

        return new CasesInfo(generateCasesFromCaseResponse(getCaseResponse(request).getCases()),
                populateCaseMetadata(caseResponseJson.getMetadata()));
    }

    private CaseRequest prepareCaseRequest(Integer pageSize, Integer pageNumber) {
        CaseRequest request = new CaseRequest();
        request.setLimit(pageSize);
        request.setOffset(pageNumber > 0 ? (pageNumber - 1) * pageSize : 0);

        return request;
    }

    private CaseResponseJson getCaseResponse(CaseRequest caseRequest) {
        String response = commcareHttpClient.casesRequest(caseRequest);
        return parseCasesFromResponse(response);
    }

    private CaseResponseJson parseCasesFromResponse(String response) {
        Type caseResponseType = new TypeToken<CaseResponseJson>() { } .getType();
        return (CaseResponseJson) motechJsonReader.readFromString(response, caseResponseType);
    }

    private CaseJson parseSingleCaseFromResponse(String response) {
        CaseJson caseReturned = null;

        try {
            caseReturned = (CaseJson) motechJsonReader
                    .readFromString(response, CaseJson.class);
        } catch (Exception e) {
            logger.warn("Exception while trying to read in case JSON: " + e.getMessage());
        }

        return caseReturned;
    }

    private List<CaseInfo> generateCasesFromCaseResponse(
            List<CaseJson> caseResponses) {
        List<CaseInfo> caseList = new ArrayList<>();

        if (caseResponses == null) {
            return Collections.emptyList();
        }

        for (CaseJson caseResponse : caseResponses) {
            caseList.add(populateCaseInfo(caseResponse));
        }

        return caseList;
    }

    private CaseInfo generateCaseFromCaseResponse(CaseJson caseResponse) {
        return populateCaseInfo(caseResponse);
    }

    private CaseInfo populateCaseInfo(CaseJson caseResponse) {
        if (caseResponse == null) {
            return null;
        }

        CaseInfo caseInfo = new CaseInfo();

        Map<String, String> properties = caseResponse.getProperties();

        String caseType = properties.get("case_type");
        String dateOpened = properties.get("date_opened");
        String ownerId = properties.get("owner_id)");
        String caseName = properties.get("case_name");

        caseInfo.setCaseType(caseType);
        caseInfo.setDateOpened(dateOpened);
        caseInfo.setOwnerId(ownerId);
        caseInfo.setCaseName(caseName);

        properties.remove("case_type");
        properties.remove("date_opened");
        properties.remove("owner_id");
        properties.remove("case_name");

        caseInfo.setFieldValues(properties);
        caseInfo.setClosed(caseResponse.isClosed());
        caseInfo.setDateClosed(caseResponse.getDateClosed());
        caseInfo.setDomain(caseResponse.getDomain());
        caseInfo.setIndices(caseResponse.getIndices());
        caseInfo.setServerDateModified(caseResponse.getServerDateModified());
        caseInfo.setServerDateOpened(caseResponse.getServerDateOpened());
        caseInfo.setVersion(caseResponse.getVersion());
        caseInfo.setXformIds(caseResponse.getXformIds());
        caseInfo.setCaseId(caseResponse.getCaseId());
        caseInfo.setUserId(caseResponse.getUserId());

        return caseInfo;
    }

    private CommcareMetadataInfo populateCaseMetadata(CommcareMetadataJson metadataJson) {
        CommcareMetadataInfo metadataInfo = new CommcareMetadataInfo();

        metadataInfo.setLimit(metadataJson.getLimit());
        metadataInfo.setNextPageQueryString(metadataJson.getNextPageQueryString());
        metadataInfo.setOffset(metadataJson.getOffset());
        metadataInfo.setPreviousPageQueryString(metadataJson.getPreviousPageQueryString());
        metadataInfo.setTotalCount(metadataJson.getTotalCount());

        return metadataInfo;
    }

    @Override
    public OpenRosaResponse uploadCase(CaseTask caseTask) {

        String caseXml = converter.convertToCaseXml(caseTask);
        String fullXml = "<?xml version='1.0'?>\n" + caseXml;

        OpenRosaResponse response;

        try {
            response = commcareHttpClient.caseUploadRequest(fullXml);
        } catch (CaseParserException e) {
            return null;
        }

        return response;
    }
}
