package org.motechproject.batch.service;

import org.motechproject.batch.exception.BatchException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service to update the .xml defining the job in jsr-352 format.
 * 
 * @author naveen
 * 
 */
public interface FileUploadService {

    /**
     * uploads the xml to the location specified in properties
     * 
     * @param jobName
     * @param file
     * @param xmlPath
     * @throws BatchException
     */
    void uploadFile(String jobName, MultipartFile file, String xmlPath)
            throws BatchException;

}
