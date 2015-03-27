package org.motechproject.csd.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.csd.client.CSDHttpClient;
import org.motechproject.csd.client.SOAPClient;
import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.CommunicationProtocol;
import org.motechproject.csd.domain.Config;
import org.motechproject.csd.domain.FacilityDirectory;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.domain.ServiceDirectory;
import org.motechproject.csd.mds.CSDDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ConfigService;
import org.motechproject.csd.service.FacilityDirectoryService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.service.ProviderDirectoryService;
import org.motechproject.csd.service.ServiceDirectoryService;
import org.motechproject.csd.util.MarshallUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.Date;
import java.util.List;

@Service("csdService")
public class CSDServiceImpl implements CSDService {

    @Autowired
    private CSDDataService csdDataService;

    @Autowired
    private FacilityDirectoryService facilityDirectoryService;

    @Autowired
    private ProviderDirectoryService providerDirectoryService;

    @Autowired
    private OrganizationDirectoryService organizationDirectoryService;

    @Autowired
    private ServiceDirectoryService serviceDirectoryService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CSDHttpClient csdHttpClient;

    @Autowired
    private SOAPClient soapClient;

    @Override
    public CSD create(CSD csd) {
        return csdDataService.create(csd);
    }

    @Override
    public CSD getCSD() {
        List<CSD> csdList = csdDataService.retrieveAll();

        if (csdList.size() > 1) {
            throw new IllegalArgumentException("In the database can be only one CSD element");
        }

        if (csdList.isEmpty()) {
            return null;
        } else {
            return csdList.get(0);
        }
    }

    @Override
    public CSD getByLastModified(DateTime lastModified) {
        FacilityDirectory facilityDirectory = new FacilityDirectory(
                facilityDirectoryService.getModifiedAfter(lastModified));
        ProviderDirectory providerDirectory = new ProviderDirectory(
                providerDirectoryService.getModifiedAfter(lastModified));
        OrganizationDirectory organizationDirectory = new OrganizationDirectory(
                organizationDirectoryService.getModifiedAfter(lastModified));
        ServiceDirectory serviceDirectory = new ServiceDirectory(
                serviceDirectoryService.getModifiedAfter(lastModified));

        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }

    @Override
    public CSD update(CSD csd) {
        if (csd != null) {
            CSD oldCSD = getCSD();

            if (oldCSD != null) {
                facilityDirectoryService.update(csd.getFacilityDirectory());
                providerDirectoryService.update(csd.getProviderDirectory());
                organizationDirectoryService.update(csd.getOrganizationDirectory());
                serviceDirectoryService.update(csd.getServiceDirectory());
            } else {
                return create(csd);
            }

            return oldCSD;
        }
        return null;
    }

    @Override
    public void delete() {
        csdDataService.deleteAll();
    }

    @Override
    public String getXmlContent() {
        return csdDataService.doInTransaction(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus transactionStatus) {
                try {
                    CSD csd = getCSD();
                    if (csd != null) {
                        return MarshallUtils.marshall(csd, CSDConstants.CSD_SCHEMA, CSD.class);
                    } else {
                        throw new IllegalArgumentException("There is no CSD structure in the database");
                    }
                } catch (SAXException e) {
                    throw new IllegalArgumentException("Invalid schema", e);
                } catch (JAXBException e) {
                    throw new IllegalArgumentException("Invalid CSD structure", e);
                }
            }
        });
    }

    @Override
    public void fetchAndUpdate() {
        Config config = configService.getConfig();
        String xmlUrl = config.getXmlUrl();
        CommunicationProtocol communicationProtocol = config.getCommunicationProtocol();

        if (xmlUrl == null) {
            throw new IllegalArgumentException("The CSD Registry URL is empty");
        }

        if (communicationProtocol.equals(CommunicationProtocol.REST)) {
            String xml = csdHttpClient.getXml(xmlUrl);
            if (xml == null) {
                throw new IllegalArgumentException("Couldn't load XML");
            }
            saveFromXml(xml);
        } else {
            DateTime lastModified;
            if (StringUtils.isNotEmpty(config.getLastModified())) {
                lastModified = DateTime.parse(config.getLastModified(),
                        DateTimeFormat.forPattern(Config.DATE_TIME_PICKER_FORMAT));
            } else {
                lastModified = new DateTime(new Date(0));
            }
            CSD csd = soapClient.getModifications(xmlUrl, lastModified).getCSD();
            update(csd);
            config.setLastModified(DateTime.now().toString(Config.DATE_TIME_PICKER_FORMAT));
        }
    }

    public void saveFromXml(String xml) {
        CSD csd;
        try {
            csd = (CSD) MarshallUtils.unmarshall(xml, CSDConstants.CSD_SCHEMA, CSD.class);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Invalid schema", e);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Invalid XML structure", e);
        }

        update(csd);
    }
}
