package org.motechproject.csd.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.csd.domain.Address;
import org.motechproject.csd.domain.AddressLine;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.CodedType;
import org.motechproject.csd.domain.ContactPoint;
import org.motechproject.csd.domain.Credential;
import org.motechproject.csd.domain.DayOfTheWeek;
import org.motechproject.csd.domain.Extension;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityDirectory;
import org.motechproject.csd.domain.FacilityOrganization;
import org.motechproject.csd.domain.FacilityOrganizationService;
import org.motechproject.csd.domain.FacilityOrganizations;
import org.motechproject.csd.domain.Geocode;
import org.motechproject.csd.domain.Name;
import org.motechproject.csd.domain.OperatingHours;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationContact;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.domain.OtherID;
import org.motechproject.csd.domain.OtherName;
import org.motechproject.csd.domain.Person;
import org.motechproject.csd.domain.PersonName;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.domain.ProviderFacilities;
import org.motechproject.csd.domain.ProviderFacility;
import org.motechproject.csd.domain.ProviderFacilityService;
import org.motechproject.csd.domain.ProviderOrganization;
import org.motechproject.csd.domain.ProviderOrganizations;
import org.motechproject.csd.domain.Record;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;

import java.util.Arrays;

public class InitialData {

    public static CSD getInitialData() {

        DateTime date = DateTime.parse("2015-03-05");
        DateTime dateTime = DateTime.parse("2015-03-05T11:16:53.461+01:00");
        DateTime time = DateTime.parse("11:16:53", DateTimeFormat.forPattern("HH:mm:ss"));

        Extension extension = new Extension("type", "urn");
        Geocode geocode = new Geocode(1, 2, 3, "c");
        OtherName otherName = new OtherName("value", "lang");
        OtherID otherID = new OtherID("code", "assigningAuthorityName");
        CodedType codedType = new CodedType("value", "code", "codingScheme");
        ContactPoint contactPoint = new ContactPoint(codedType, "equipment", "purpose", "certificate");
        Credential credential = new Credential(codedType, "number", "issuingAuthority", date, date, Arrays.asList(extension));
        OperatingHours operatingHours = new OperatingHours(true, Arrays.asList(DayOfTheWeek.THURSDAY), time, time, date, date);

        AddressLine addressLine = new AddressLine("value", "component");
        Address address = new Address(Arrays.asList(addressLine, addressLine), "type");

        Record record = new Record(dateTime, dateTime, "status", "sourceDirectory");

        Name name = new Name(Arrays.asList("name"), "honorific", "forename", Arrays.asList(codedType), "surname", "suffix");
        PersonName personName = new PersonName(Arrays.asList("commonName"), "honorific", "forename", Arrays.asList(codedType), "surname", "suffix", "lang");
        Person person = new Person(Arrays.asList(personName), Arrays.asList(contactPoint, contactPoint), Arrays.asList(address), "gender", date);

        OrganizationContact organizationContactPerson = new OrganizationContact(person);
        OrganizationContact organizationContactProvider = new OrganizationContact("ProviderEntityID");

        Organization organization = new Organization("entityID", Arrays.asList(credential), "orgID", Arrays.asList(codedType), Arrays.asList(otherID),
                "organization_primaryName", Arrays.asList(otherName), Arrays.asList(address), Arrays.asList(organizationContactPerson,
                organizationContactProvider), Arrays.asList(codedType), Arrays.asList(contactPoint), Arrays.asList(codedType), Arrays.asList(extension), record);

        FacilityOrganizationService facilityOrganizationService = new FacilityOrganizationService("entityID", Arrays.asList(name),
                Arrays.asList(codedType), Arrays.asList(operatingHours), "freeBusyURI", Arrays.asList(extension));
        FacilityOrganization facilityOrganization = new FacilityOrganization("entityID", Arrays.asList(facilityOrganizationService), Arrays.asList(extension));
        FacilityOrganizations facilityOrganizations = new FacilityOrganizations(Arrays.asList(facilityOrganization));

        Facility facility = new Facility("entityID", Arrays.asList(codedType), Arrays.asList(extension), record, Arrays.asList(otherID),
                "primaryName", Arrays.asList(otherName), Arrays.asList(address), Arrays.asList(organizationContactPerson),
                Arrays.asList(codedType), Arrays.asList(contactPoint), geocode, facilityOrganizations, Arrays.asList(operatingHours));

        ProviderOrganization providerOrganization = new ProviderOrganization("entityID", Arrays.asList(extension), Arrays.asList(address), Arrays.asList(contactPoint));
        ProviderOrganizations providerOrganizations = new ProviderOrganizations(Arrays.asList(providerOrganization));

        ProviderFacilityService providerFacilityService = new ProviderFacilityService("entityID", Arrays.asList(name),
                "provOrgEntityID", Arrays.asList(codedType), Arrays.asList(operatingHours), "freeBusyURI", Arrays.asList(extension));
        ProviderFacility providerFacility = new ProviderFacility("entityID", Arrays.asList(providerFacilityService), Arrays.asList(operatingHours), Arrays.asList(extension));
        ProviderFacilities providerFacilities = new ProviderFacilities(Arrays.asList(providerFacility));

        Provider provider = new Provider("entityID", Arrays.asList(codedType), Arrays.asList(extension), record, Arrays.asList(otherID),
                person, Arrays.asList(codedType), providerOrganizations, providerFacilities, Arrays.asList(credential), Arrays.asList(codedType));

        OrganizationDirectory organizationDirectory = new OrganizationDirectory(Arrays.asList(organization));
        ServiceDirectory serviceDirectory = new ServiceDirectory(Arrays.asList(new Service("entityID", codedType, Arrays.asList(extension), record)));
        FacilityDirectory facilityDirectory = new FacilityDirectory(Arrays.asList(facility));
        ProviderDirectory providerDirectory = new ProviderDirectory(Arrays.asList(provider));

        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }
}
