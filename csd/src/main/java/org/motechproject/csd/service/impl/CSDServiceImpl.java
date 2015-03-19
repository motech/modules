package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.FacilityDirectory;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.domain.ServiceDirectory;
import org.motechproject.csd.mds.CSDDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.FacilityDirectoryService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.service.ProviderDirectoryService;
import org.motechproject.csd.service.ServiceDirectoryService;
import org.motechproject.csd.util.MarshallUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
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
    public void saveFromXml(String xml) {

        CSD csd;

        try {
            csd = (CSD) MarshallUtils.unmarshall(xml, getClass().getResource("/CSD.xsd"), CSD.class);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Cannot load Schema", e);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Cannot unmarshall xml", e);
        }

        update(csd);
    }
}
