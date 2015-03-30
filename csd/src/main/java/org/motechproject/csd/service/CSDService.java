package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.CSD;

public interface CSDService {

    CSD create(CSD csd);

    CSD getCSD();

    CSD getByLastModified(DateTime lastModified);

    CSD update(CSD csd);

    void saveFromXml(String xml);

    String getXmlContent();
}
