package org.motechproject.csd.service.impl;

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
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ConfigService;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.csd.service.OrganizationService;
import org.motechproject.csd.service.ProviderService;
import org.motechproject.csd.service.ServiceService;
import org.motechproject.csd.util.MarshallUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.HashSet;

@Service("csdService")
public class CSDServiceImpl implements CSDService {

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CSDHttpClient csdHttpClient;

    @Autowired
    private SOAPClient soapClient;

    @Override
    @Transactional
    public CSD getCSD() {
        FacilityDirectory facilityDirectory = new FacilityDirectory(new HashSet<>(facilityService.allFacilities()));
        OrganizationDirectory organizationDirectory = new OrganizationDirectory(new HashSet<>(organizationService.allOrganizations()));
        ProviderDirectory providerDirectory = new ProviderDirectory(new HashSet<>(providerService.allProviders()));
        ServiceDirectory serviceDirectory = new ServiceDirectory(new HashSet<>(serviceService.allServices()));
        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }

    @Override
    @Transactional
    public CSD getByLastModification(DateTime lastModified) {
        FacilityDirectory facilityDirectory = new FacilityDirectory(facilityService.getModifiedAfter(lastModified));
        ProviderDirectory providerDirectory = new ProviderDirectory(providerService.getModifiedAfter(lastModified));
        OrganizationDirectory organizationDirectory = new OrganizationDirectory(organizationService.getModifiedAfter(lastModified));
        ServiceDirectory serviceDirectory = new ServiceDirectory(serviceService.getModifiedAfter(lastModified));

        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }

    @Override
    @Transactional
    public void update(CSD csd) {
        if (csd != null) {
            if (csd.getFacilityDirectory() != null && csd.getFacilityDirectory().getFacilities() != null) {
                facilityService.update(csd.getFacilityDirectory().getFacilities());
            }
            if (csd.getProviderDirectory() != null && csd.getProviderDirectory().getProviders() != null) {
                providerService.update(csd.getProviderDirectory().getProviders());
            }
            if (csd.getOrganizationDirectory() != null && csd.getOrganizationDirectory().getOrganizations() != null) {
                organizationService.update(csd.getOrganizationDirectory().getOrganizations());
            }
            if (csd.getServiceDirectory() != null && csd.getServiceDirectory().getServices() != null) {
                serviceService.update(csd.getServiceDirectory().getServices());
            }
        }
    }

    @Override
    @Transactional
    public void delete() {
        facilityService.deleteAll();
        organizationService.deleteAll();
        providerService.deleteAll();
        serviceService.deleteAll();
    }

    @Override
    public String getXmlContent() {
        try {
            CSD csd = getCSD();
            if (csd != null) {
                return MarshallUtils.marshall(csd, CSDConstants.CSD_SCHEMA, CSD.class);
            } else {
                throw new IllegalStateException("There is no CSD structure in the database");
            }
        } catch (SAXException e) {
            throw new IllegalStateException("Invalid schema", e);
        } catch (JAXBException e) {
            throw new IllegalStateException("Invalid CSD structure", e);
        }
    }

    @Override
    @Transactional
    public void fetchAndUpdate(String xmlUrl) {
        Config config = configService.getConfig(xmlUrl);
        CommunicationProtocol communicationProtocol = config.getCommunicationProtocol();

        if (xmlUrl == null) {
            throw new IllegalArgumentException("The CSD Registry URL is empty");
        }

        if (communicationProtocol.equals(CommunicationProtocol.REST)) {
            fetchAndUpdateUsingREST(xmlUrl);
        } else {
            DateTime lastModified = DateTime.parse(config.getLastModified(),
                    DateTimeFormat.forPattern(Config.DATE_TIME_PICKER_FORMAT));
            fetchAndUpdateUsingSOAP(xmlUrl, lastModified);
            config.setLastModified(DateTime.now().toString(Config.DATE_TIME_PICKER_FORMAT));
        }
    }

    @Override
    public void fetchAndUpdateUsingREST(String xmlUrl) {
        String xml = csdHttpClient.getXml(xmlUrl);
        if (xml == null) {
            throw new IllegalArgumentException("Couldn't load XML from url: " + xmlUrl);
        }
        saveFromXml(xml);
    }

    @Override
    public void fetchAndUpdateUsingSOAP(String xmlUrl, DateTime lastModified) {
        CSD csd = soapClient.getModifications(xmlUrl, lastModified).getCSD();
        update(csd);
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
