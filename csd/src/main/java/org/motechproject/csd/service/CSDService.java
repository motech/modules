package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.CSD;

public interface CSDService {

    CSD getCSD();

    CSD getByLastModification(DateTime lastModified);

    void update(CSD csd);

    void delete();

    String getXmlContent();

    void fetchAndUpdate(String xmlUrl);

    void fetchAndUpdateUsingREST(String xmlUrl);

    void fetchAndUpdateUsingSOAP(String xmlUrl, DateTime lastModified);

    void saveFromXml(String xml);
}
