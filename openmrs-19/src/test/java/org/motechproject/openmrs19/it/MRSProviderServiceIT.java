package org.motechproject.openmrs19.it;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.motechproject.openmrs19.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSProviderServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSProviderService providerService;

    @Inject
    private OpenMRSPersonService personService;

    Provider provider;

    @Test
    public void shouldSaveProvider() {

        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooFirstName");
        name.setFamilyName("FooLastName");
        person.setNames(Collections.singletonList(name));

        person.setGender("F");

        person = personService.createPerson(DEFAULT_CONFIG_NAME, person);

        Provider provider = new Provider();
        provider.setIdentifier("FooIdentifier");
        provider.setPerson(person);

        this.provider = providerService.createProvider(DEFAULT_CONFIG_NAME, provider);

        assertNotNull(this.provider);
        assertEquals(this.provider.getPerson().getUuid(), person.getUuid());
        assertEquals(this.provider.getIdentifier(), provider.getIdentifier());

    }

    @After
    public void tearDown() {

        if (provider != null) {
            String uuid = provider.getPerson().getUuid();

            providerService.deleteProvider(DEFAULT_CONFIG_NAME, provider.getUuid());
            personService.deletePerson(DEFAULT_CONFIG_NAME, uuid);
        }
    }

}
