package org.motechproject.openmrs19.it;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSProviderServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSProviderService providerService;

    @Inject
    private OpenMRSPersonService personService;

    OpenMRSProvider provider;

    @Test
    public void shouldSaveProvider() {

        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("FooFirstName");
        person.setLastName("FooLastName");
        person.setGender("F");
        person = personService.createPerson(person);

        OpenMRSProvider provider = new OpenMRSProvider();
        provider.setIdentifier("FooIdentifier");
        provider.setPerson(person);

        this.provider = providerService.createProvider(provider);

        assertNotNull(this.provider);
        assertEquals(this.provider.getPerson().getPersonId(), person.getPersonId());
        assertEquals(this.provider.getIdentifier(), provider.getIdentifier());

    }

    @After
    public void tearDown() {

        if (provider != null) {
            String uuid = provider.getPerson().getPersonId();

            providerService.deleteProvider(provider.getProviderId());
            personService.deletePerson(uuid);
        }
    }

}
