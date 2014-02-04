package org.motechproject.ivr.service;

import org.motechproject.commons.couchdb.query.QueryParam;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;
import org.motechproject.ivr.domain.CallDisposition;
import org.motechproject.ivr.domain.CallRecordSearchParameters;
import org.motechproject.ivr.repository.AllCallDetailRecords;
import org.motechproject.ivr.service.contract.CallRecordsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides convenient methods for searching call records.
 */

@Service
public class CallRecordsSearchServiceImpl implements CallRecordsSearchService {
    private AllCallDetailRecords allCallDetailRecords;

    @Autowired
    public CallRecordsSearchServiceImpl(AllCallDetailRecords allCallDetailRecords) {
        this.allCallDetailRecords = allCallDetailRecords;
    }

    @Override
    public List<CallDetailRecord> search(CallRecordSearchParameters callLogSearchParameters) {
        QueryParam queryParam = callLogSearchParameters.getQueryParam();
        return allCallDetailRecords.search(callLogSearchParameters.getPhoneNumber(),
                callLogSearchParameters.getStartFromDateAsDateTime(),
                callLogSearchParameters.getStartToDateAsDateTime(),
                callLogSearchParameters.getAnswerFromDateAsDateTime(),
                callLogSearchParameters.getAnswerToDateAsDateTime(),
                callLogSearchParameters.getEndFromDateAsDateTime(),
                callLogSearchParameters.getEndToDateAsDateTime(),
                callLogSearchParameters.getMinDuration(),
                callLogSearchParameters.getMaxDuration(),
                mapToDispositions(callLogSearchParameters), mapToDirections(callLogSearchParameters),
                queryParam.getSortBy(), queryParam.isReverse());
    }

    /**
     *
     * @param callLogSearchParameters
     * @return the number of pages which fit the given call record search parameters
     */
    @Override
    public long count(CallRecordSearchParameters callLogSearchParameters) {
        double numOfPages = allCallDetailRecords.countRecords(callLogSearchParameters.getPhoneNumber(),
                callLogSearchParameters.getStartFromDateAsDateTime(),
                callLogSearchParameters.getStartToDateAsDateTime(),
                callLogSearchParameters.getAnswerFromDateAsDateTime(),
                callLogSearchParameters.getAnswerToDateAsDateTime(),
                callLogSearchParameters.getEndFromDateAsDateTime(),
                callLogSearchParameters.getEndToDateAsDateTime(),
                callLogSearchParameters.getMinDuration(),
                callLogSearchParameters.getMaxDuration(), mapToDispositions(callLogSearchParameters),
                mapToDirections(callLogSearchParameters)) /
                (callLogSearchParameters.getQueryParam().getRecordsPerPage() * 1.0);
        return Math.round(Math.ceil(numOfPages));
    }

    @Override
    public List<String> getAllPhoneNumbers() {
        return allCallDetailRecords.getAllPhoneNumbers();
    }

    @Override
    public long findMaxCallDuration() {
        return allCallDetailRecords.findMaxCallDuration();
    }

    //Takes the given Call Record Search Parameters and returns a list of all dispositions
    //in the parameters
    private List<String> mapToDispositions(CallRecordSearchParameters callLogSearchParameters) {
        List<String> dispositions = new ArrayList<>();

        if (callLogSearchParameters.getAnswered()) {
            dispositions.add(CallDisposition.ANSWERED.name());
        }
        if (callLogSearchParameters.getBusy()) {
            dispositions.add(CallDisposition.BUSY.name());
        }
        if (callLogSearchParameters.getFailed()) {
            dispositions.add(CallDisposition.FAILED.name());
        }
        if (callLogSearchParameters.getNoAnswer()) {
            dispositions.add(CallDisposition.NO_ANSWER.name());
        }
        if (callLogSearchParameters.getUnknown()) {
            dispositions.add(CallDisposition.UNKNOWN.name());
        }
        return dispositions;
    }

    //Takes the given Call Record Search Parameters and returns a list of all directions
    //in the parameters
    private List<String> mapToDirections(CallRecordSearchParameters callLogSearchParameters) {
        List<String> directions = new ArrayList<>();
        if (callLogSearchParameters.isInbound()) {
            directions.add(CallDirection.Inbound.name());
        }
        if (callLogSearchParameters.isOutbound()) {
            directions.add(CallDirection.Outbound.name());
        }
        return directions;
    }
}
