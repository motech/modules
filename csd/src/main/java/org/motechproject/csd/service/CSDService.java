package org.motechproject.csd.service;

import org.motechproject.csd.domain.CSD;

public interface CSDService {

    CSD create(CSD csd);

    CSD getCSD();

    CSD update(CSD csd);

    void saveFromXml(String xml);
}
