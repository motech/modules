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

        DateTime dateTime = DateTime.parse("2015-03-05T11:16:00.000+01:00");

        OrganizationDirectory organizationDirectory = new OrganizationDirectory(new HashSet<>(Arrays.asList(createOrganization(dateTime, "organizationEntityID"))));
        ServiceDirectory serviceDirectory = new ServiceDirectory(new HashSet<>(Arrays.asList(createService(dateTime, "serviceEntityID"))));
        FacilityDirectory facilityDirectory = new FacilityDirectory(new HashSet<>(Arrays.asList(createFacility(dateTime, "FacilityEntityID"))));
        ProviderDirectory providerDirectory = new ProviderDirectory(new HashSet<>(Arrays.asList(createProvider(dateTime, "providerEntityID"))));

        return new CSD(organizationDirectory, serviceDirectory, facilityDirectory, providerDirectory);
    }

    public static Facility createFacility(DateTime updated, String entityID) {
        return new Facility(entityID, new HashSet<>(Arrays.asList(new CodedType("facilityCodedType", "c"))),
                new HashSet<>(Arrays.asList(new Extension("facilityExtension", "urn"))), new Record(updated, updated, "status", "facility"),
                new HashSet<>(Arrays.asList(new OtherID("facility", "a"))), "facility", new HashSet<>(Arrays.asList(new OtherName("facility", "en"))),
                new HashSet<>(Arrays.asList(createAddress())), new HashSet<>(Arrays.asList(new OrganizationContact(createPerson()))),
                new HashSet<>(Arrays.asList(new CodedType("facility", "c"))), new HashSet<>(Arrays.asList(createContactPoint())),
                new Geocode(1, 2, 3, "c"), new FacilityOrganizations(new HashSet<>(Arrays.asList(createFacilityOrganization()))),
                new HashSet<>(Arrays.asList(createOperatingHours())));
    }

    public static Organization createOrganization(DateTime updated, String entityID) {
        return new Organization(entityID, new HashSet<>(Arrays.asList(createCredential())), "orgID", new HashSet<>(Arrays.asList(new CodedType("org", "c"))),
                new HashSet<>(Arrays.asList(new OtherID("org", "a"))), "organization_primaryName", new HashSet<>(Arrays.asList(new OtherName("organization", "en"))),
                new HashSet<>(Arrays.asList(createAddress())), new HashSet<>(Arrays.asList(new OrganizationContact("providerEntityID"))),
                new HashSet<>(Arrays.asList(new CodedType("org", "c"))), new HashSet<>(Arrays.asList(createContactPoint())),
                new HashSet<>(Arrays.asList(new CodedType("org", "c"))), new HashSet<>(Arrays.asList(new Extension("org", "urn"))),
                new Record(updated, "status", updated));
    }

    public static Provider createProvider(DateTime updated, String entityID) {
        return new Provider(entityID, new HashSet<>(Arrays.asList(new CodedType("provider", "c"))), new HashSet<>(Arrays.asList(new Extension("providerExtension", "urn"))),
                new Record(updated, "status", updated), new HashSet<>(Arrays.asList(new OtherID("code", "a"))), createPerson(), new HashSet<>(Arrays.asList(new CodedType("c", "c"))),
                new ProviderOrganizations(new HashSet<>(Arrays.asList(createProviderOrganization()))), new ProviderFacilities(new HashSet<>(Arrays.asList(createProviderFacility()))),
                new HashSet<>(Arrays.asList(createCredential())), new HashSet<>(Arrays.asList(new CodedType("c", "c"))));
    }

    public static Service createService(DateTime updated, String entityID) {
        return new Service(entityID, new CodedType("service", "c"),
                new HashSet<>(Arrays.asList(new Extension("serviceExtension", "urn"))), new Record(updated, "s", updated));
    }

    private static ContactPoint createContactPoint() {
        return new ContactPoint(new CodedType("c", "c"), "equipment", "purpose", "certificate");
    }

    private static Person createPerson() {
        return new Person(new HashSet<>(Arrays.asList(new PersonName(new ArrayList<>(Arrays.asList("commonName")), "honorific",
                "forename", new HashSet<>(Arrays.asList(new CodedType("code", "c"))), "surname", "suffix", "lang"))),
                new HashSet<>(Arrays.asList(createContactPoint())), new HashSet<>(Arrays.asList(createAddress())), "gender", DateTime.parse("2015-03-05"));
    }

    private static Address createAddress() {
        return new Address(new HashSet<>(Arrays.asList(new AddressLine("addressLine", "component"))), "type");
    }

    private static OperatingHours createOperatingHours() {
        return new OperatingHours(true, new ArrayList<>(Arrays.asList(DayOfTheWeek.THURSDAY)), DateTime.parse("11:00:00", DateTimeFormat.forPattern("HH:mm:ss")),
                DateTime.parse("18:00:00", DateTimeFormat.forPattern("HH:mm:ss")), DateTime.parse("2015-03-05"), DateTime.parse("2020-03-05"));
    }

    private static Credential createCredential() {
        return new Credential(new CodedType("code", "c"), "number", "issuingAuthority", DateTime.parse("2015-03-05"),
                DateTime.parse("2015-03-05"), new HashSet<>(Arrays.asList(new Extension("type", "urn"))));
    }

    private static FacilityOrganization createFacilityOrganization() {
        return new FacilityOrganization("entityID", new HashSet<>(Arrays.asList(new FacilityOrganizationService("entityID", new HashSet<>(Arrays.asList(createName())),
                new HashSet<>(Arrays.asList(new CodedType("c", "c"))), new HashSet<>(Arrays.asList(createOperatingHours())), "freeBusyURI",
                new HashSet<>(Arrays.asList(new Extension("extension", "urn")))))), new HashSet<>(Arrays.asList(new Extension("extension", "urn"))));
    }

    private static Name createName() {
        return new Name(new ArrayList<>(Arrays.asList("name")), "honorific", "forename",
                new HashSet<>(Arrays.asList(new CodedType("code", "c"))), "surname", "suffix");
    }

    private static ProviderFacility createProviderFacility() {
        return new ProviderFacility("entityID", new HashSet<>(Arrays.asList(new ProviderFacilityService("entityID", new HashSet<>(Arrays.asList(createName())),
                "provOrgEntityID", new HashSet<>(Arrays.asList(new CodedType("c", "c"))), new HashSet<>(Arrays.asList(createOperatingHours())),
                "freeBusyURI", new HashSet<>(Arrays.asList(new Extension("extension", "urn")))))), new HashSet<>(Arrays.asList(createOperatingHours())),
                new HashSet<>(Arrays.asList(new Extension("extension", "urn"))));
    }

    private static ProviderOrganization createProviderOrganization() {
        return new ProviderOrganization("entityID", new HashSet<>(Arrays.asList(new Extension("extension", "urn"))),
                new HashSet<>(Arrays.asList(createAddress())), new HashSet<>(Arrays.asList(createContactPoint())));
    }
}
