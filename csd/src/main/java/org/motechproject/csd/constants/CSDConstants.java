package org.motechproject.csd.constants;

import org.motechproject.csd.domain.AbstractID;
import org.motechproject.csd.domain.AbstractUniqueID;
import org.motechproject.csd.domain.Address;
import org.motechproject.csd.domain.AddressLine;
import org.motechproject.csd.domain.BaseMainEntity;
import org.motechproject.csd.domain.CodedType;
import org.motechproject.csd.domain.ContactPoint;
import org.motechproject.csd.domain.Credential;
import org.motechproject.csd.domain.Extension;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityOrganization;
import org.motechproject.csd.domain.FacilityOrganizationService;
import org.motechproject.csd.domain.Geocode;
import org.motechproject.csd.domain.Name;
import org.motechproject.csd.domain.OperatingHours;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationContact;
import org.motechproject.csd.domain.OtherID;
import org.motechproject.csd.domain.OtherName;
import org.motechproject.csd.domain.Person;
import org.motechproject.csd.domain.PersonName;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderFacility;
import org.motechproject.csd.domain.ProviderFacilityService;
import org.motechproject.csd.domain.ProviderOrganization;
import org.motechproject.csd.domain.Record;
import org.motechproject.csd.domain.Service;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public final class CSDConstants {

    public static final URL CSD_SCHEMA = CSDConstants.class.getResource("/CSD.xsd");
    public static final String MANAGE_CSD = "manageCSD";
    public static final String HAS_MANAGE_CSD_PERMISSION = "hasRole('manageCSD')";

    public static final List<Class<?>> CSD_CLASSES = Arrays.asList(AbstractID.class, AbstractUniqueID.class, Address.class,
            AddressLine.class, BaseMainEntity.class, CodedType.class, ContactPoint.class, Credential.class,
            Extension.class, Facility.class, FacilityOrganization.class, FacilityOrganizationService.class,
            Geocode.class, Name.class, OperatingHours.class, Organization.class, OrganizationContact.class,
            OtherID.class, OtherName.class, Person.class, PersonName.class, Provider.class, Service.class,
            ProviderFacility.class, ProviderFacilityService.class, ProviderOrganization.class, Record.class);

    private CSDConstants() {
    }
}
