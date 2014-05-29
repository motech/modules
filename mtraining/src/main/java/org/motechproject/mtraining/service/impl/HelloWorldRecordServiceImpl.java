package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.EnrollmentRecord;
import org.motechproject.mtraining.service.HelloWorldRecordService;
import org.motechproject.mtraining.repository.HelloWorldRecordsDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link HelloWorldRecordService} interface. Uses
 * {@link HelloWorldRecordsDataService} in order to retrieve and persist records.
 */
@Service("helloWorldRecordService")
public class HelloWorldRecordServiceImpl implements HelloWorldRecordService {

    @Autowired
    private HelloWorldRecordsDataService helloWorldRecordsDataService;

    @Override
    public void create(String name, String message) {
        helloWorldRecordsDataService.create(
                new EnrollmentRecord(name, message)
        );
    }

    @Override
    public void add(EnrollmentRecord record) {
        helloWorldRecordsDataService.create(record);
    }

    @Override
    public EnrollmentRecord findRecordByName(String recordName) {
        EnrollmentRecord record = helloWorldRecordsDataService.findRecordByName(recordName);
        if (null == record) {
            return null;
        }
        return record;
    }

    @Override
    public List<EnrollmentRecord> getRecords() {
        return helloWorldRecordsDataService.retrieveAll();
    }

    @Override
    public void update(EnrollmentRecord record) {
        helloWorldRecordsDataService.update(record);
    }

    @Override
    public void delete(EnrollmentRecord record) {
        helloWorldRecordsDataService.delete(record);
    }
}
