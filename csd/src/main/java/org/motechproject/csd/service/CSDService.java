package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.CSD;

public interface CSDService {

    CSD getCSD();

    CSD getByLastModified(DateTime lastModified);

    void update(CSD csd);

    void delete();

    String getXmlContent();

    void fetchAndUpdate(String xmlUrl);

    void saveFromXml(String xml);
}
