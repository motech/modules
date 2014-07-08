package org.motechproject.batch.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.motechproject.batch.exception.ApplicationErrors;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.service.FileUploadService;
import org.motechproject.batch.util.BatchConstants;
import org.motechproject.batch.web.BatchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "fileUploadService")
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger LOGGER = Logger
            .getLogger(BatchController.class);

    @Autowired
    private BatchJobMDSService jobRepo;

    @Override
    public void uploadFile(String jobName, MultipartFile file, String xmlPath)
            throws BatchException {

        if (!jobNameExists(jobName)) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND,
                    ApplicationErrors.JOB_NOT_FOUND.getMessage());
        }

        LOGGER.debug("xml path" + xmlPath);
        byte[] bytes;
        BufferedOutputStream stream = null;

        try {

            // Creating folders if not present
            String location = xmlPath + BatchConstants.META_INF_PATH;
            File f = new File(location);
            if (!f.exists()) {
                f.mkdir();
            }
            location = location + BatchConstants.BATCH_JOBS_PATH;
            f = new File(location);
            if (!f.exists()) {
                f.mkdir();
            }
            bytes = file.getBytes();

            stream = new BufferedOutputStream(new FileOutputStream(new File(
                    location, jobName + BatchConstants.XML_EXTENSION)));
            stream.write(bytes);

        } catch (IOException e) {
            throw new BatchException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e,
                    ApplicationErrors.FILE_READING_WRTING_FAILED.getMessage());
        } finally {
            IOUtils.closeQuietly(stream);
        }

    }

    private boolean jobNameExists(String jobName) {
        boolean jobNameExists = true;
        List<BatchJob> jobs = jobRepo.findByJobName(jobName);
        if (jobs == null || jobs.size() == 0) {
            jobNameExists = false;
        }
        return jobNameExists;
    }

}
