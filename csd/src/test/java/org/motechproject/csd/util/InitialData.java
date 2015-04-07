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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
        Credential credential = new Credential(codedType, "number", "issuingAuthority", date, date, new HashSet<>(Arrays.asList(extension)));
        OperatingHours operatingHours = new OperatingHours(true, new ArrayList<>(Arrays.asList(DayOfTheWeek.THURSDAY)), time, time, date, date);

        AddressLine addressLine = new AddressLine("value", "component");
        Address address = new Address(new HashSet<>(Arrays.asList(addressLine)), "type");

        Record record = new Record(dateTime, dateTime, "status", "sourceDirectory");

        Name name = new Name(new ArrayList<>(Arrays.asList("name")), "honorific", "forename", new HashSet<>(Arrays.asList(codedType)), "surname", "suffix");
        PersonName personName = new PersonName(new ArrayList<>(Arrays.asList("commonName")), "honorific", "forename", new HashSet<>(Arrays.asList(codedType)), "surname", "suffix", "lang");
        Person person = new Person(new HashSet<>(Arrays.asList(personName)), new HashSet<>(Arrays.asList(contactPoint)), new HashSet<>(Arrays.asList(address)), "gender", date);

        OrganizationContact organizationContactPerson = new OrganizationContact(person);
        OrganizationContact organizationContactProvider = new OrganizationContact("ProviderEntityID");

        Organization organization = new Organization("entityID", new HashSet<>(Arrays.asList(credential)), "orgID", new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(otherID)),
                "organization_primaryName", new HashSet<>(Arrays.asList(otherName)), new HashSet<>(Arrays.asList(address)), new HashSet<>(Arrays.asList(organizationContactProvider)), new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(contactPoint)), new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(extension)), record);

        FacilityOrganizationService facilityOrganizationService = new FacilityOrganizationService("entityID", new HashSet<>(Arrays.asList(name)),
                new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(operatingHours)), "freeBusyURI", new HashSet<>(Arrays.asList(extension)));
        FacilityOrganization facilityOrganization = new FacilityOrganization("entityID", new HashSet<>(Arrays.asList(facilityOrganizationService)), new HashSet<>(Arrays.asList(extension)));
        FacilityOrganizations facilityOrganizations = new FacilityOrganizations(new HashSet<>(Arrays.asList(facilityOrganization)));

        Facility facility = new Facility("entityID", new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(extension)), record, new HashSet<>(Arrays.asList(otherID)),
                "primaryName", new HashSet<>(Arrays.asList(otherName)), new HashSet<>(Arrays.asList(address)), new HashSet<>(Arrays.asList(organizationContactPerson)),
                new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(contactPoint)), geocode, facilityOrganizations, new HashSet<>(Arrays.asList(operatingHours)));

        ProviderOrganization providerOrganization = new ProviderOrganization("entityID", new HashSet<>(Arrays.asList(extension)), new HashSet<>(Arrays.asList(address)), new HashSet<>(Arrays.asList(contactPoint)));
        ProviderOrganizations providerOrganizations = new ProviderOrganizations(new HashSet<>(Arrays.asList(providerOrganization)));

        ProviderFacilityService providerFacilityService = new ProviderFacilityService("entityID", new HashSet<>(Arrays.asList(name)),
                "provOrgEntityID", new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(operatingHours)), "freeBusyURI", new HashSet<>(Arrays.asList(extension)));
        ProviderFacility providerFacility = new ProviderFacility("entityID", new HashSet<>(Arrays.asList(providerFacilityService)), new HashSet<>(Arrays.asList(operatingHours)), new HashSet<>(Arrays.asList(extension)));
        ProviderFacilities providerFacilities = new ProviderFacilities(new HashSet<>(Arrays.asList(providerFacility)));

        Provider provider = new Provider("entityID", new HashSet<>(Arrays.asList(codedType)), new HashSet<>(Arrays.asList(extension)), record, new HashSet<>(Arrays.asList(otherID)),
                person, new HashSet<>(Arrays.asList(codedType)), providerOrganizations, providerFacilities, new HashSet<>(Arrays.asList(credential)), new HashSet<>(Arrays.asList(codedType)));

        OrganizationDirectory organizationDirectory = new OrganizationDirectory(new HashSet<>(Arrays.asList(organization)));
        ServiceDirectory serviceDirectory = new ServiceDirectory(new HashSet<>(Arrays.asList(new Service("entityID", codedType, new HashSet<>(Arrays.asList(extension)), record))));
        FacilityDirectory facilityDirectory = new FacilityDirectory(new HashSet<>(Arrays.asList(facility)));
        ProviderDirectory providerDirectory = new ProviderDirectory(new HashSet<>(Arrays.asList(provider)));

        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }
}
