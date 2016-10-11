package org.motechproject.openmrs.it;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptName;
import org.motechproject.openmrs.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSConceptServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSConceptService conceptAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    final Object lock = new Object();

    MrsListener mrsListener;

    Concept conceptOne;
    Concept conceptTwo;

    @Before
    public void setUp() throws ConceptNameAlreadyInUseException, InterruptedException {

        mrsListener = new MrsListener();

        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_CONCEPT_SUBJECT,
                EventKeys.UPDATED_CONCEPT_SUBJECT,EventKeys.DELETED_CONCEPT_SUBJECT));

        conceptTwo = createConcept(prepareConceptTwo());
        conceptOne = createConcept(prepareConceptOne());
    }

    @Test
    public void shouldCreateConcept() throws ConceptNameAlreadyInUseException {

        assertEquals(conceptOne.getName().getName(), mrsListener.eventParameters.get(EventKeys.CONCEPT_NAME));
        assertEquals(conceptOne.getDatatype().getDisplay(), mrsListener.eventParameters.get(EventKeys.CONCEPT_DATA_TYPE));
        assertEquals(conceptOne.getConceptClass().getDisplay(), mrsListener.eventParameters.get(EventKeys.CONCEPT_CONCEPT_CLASS));

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldUpdateConcept() throws InterruptedException, ConceptNameAlreadyInUseException {

        conceptOne.setDatatype(new Concept.DataType("Text"));
        conceptOne.setConceptClass(new Concept.ConceptClass("Test"));

        synchronized (lock) {
            conceptAdapter.updateConcept(DEFAULT_CONFIG_NAME, conceptOne);
            lock.wait(6000);
        }

        Concept updatedConcept = conceptAdapter.getConceptByUuid(DEFAULT_CONFIG_NAME, conceptOne.getUuid());

        assertEquals("Text", updatedConcept.getDatatype().getDisplay());
        assertEquals("Test", updatedConcept.getConceptClass().getDisplay());

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteConcept() throws InterruptedException, ConceptNameAlreadyInUseException {
        Boolean isOpenMRSExceptionThrown = Boolean.FALSE;

        synchronized (lock) {
            conceptAdapter.deleteConcept(DEFAULT_CONFIG_NAME, conceptOne.getUuid());
            try {
                conceptAdapter.getConceptByUuid(DEFAULT_CONFIG_NAME, conceptOne.getUuid());
            } catch (OpenMRSException e) {
                isOpenMRSExceptionThrown = Boolean.TRUE;
            }

            assertTrue(isOpenMRSExceptionThrown);

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetAllConcepts() throws ConceptNameAlreadyInUseException, InterruptedException {

        List<? extends Concept> concepts = conceptAdapter.getAllConcepts(DEFAULT_CONFIG_NAME);

        assertTrue(concepts.size() >= 2);
    }

    @Test
    public void shouldGetConcepts() {

        List<? extends Concept> concepts = conceptAdapter.getConcepts(DEFAULT_CONFIG_NAME, 1, 1);

        assertTrue(concepts.size() == 1);

        concepts = conceptAdapter.getConcepts(DEFAULT_CONFIG_NAME, 1, 2);

        assertTrue(concepts.size() == 2);

    }

    @Test
    public void shouldFindSingleConceptByName() throws InterruptedException, ConceptNameAlreadyInUseException {

        List<? extends Concept> concepts = conceptAdapter.search(DEFAULT_CONFIG_NAME, conceptOne.getName().getName());

        assertEquals(1, concepts.size());

        assertEquals(concepts.get(0), conceptOne);

    }

    @After
    public void tearDown() throws InterruptedException {

        deleteConcept(conceptOne);
        deleteConcept(conceptTwo);

        eventListenerRegistry.clearListenersForBean(mrsListener.getIdentifier());
    }

    private Concept prepareConceptOne() {

        Concept concept = new Concept();
        ConceptName conceptName = new ConceptName("FooConceptOne");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDatatype(new Concept.DataType("TEXT"));
        concept.setConceptClass(new Concept.ConceptClass("Test"));

        return concept;
    }

    private Concept prepareConceptTwo() {

        Concept concept = new Concept();
        ConceptName conceptName = new ConceptName("FooConceptTwo");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDatatype(new Concept.DataType("TEXT"));
        concept.setConceptClass(new Concept.ConceptClass("Test"));

        return concept;
    }

    private Concept createConcept(Concept concept) throws ConceptNameAlreadyInUseException, InterruptedException {

        Concept created;

        synchronized (lock) {
            created = conceptAdapter.createConcept(DEFAULT_CONFIG_NAME, concept);
            assertNotNull(created);
            lock.wait(60000);
        }
        return created;
    }

    private void deleteConcept(Concept concept) throws InterruptedException {
        Boolean isOpenMRSExceptionThrown = Boolean.FALSE;

        conceptAdapter.deleteConcept(DEFAULT_CONFIG_NAME, concept.getUuid());

        try {
            conceptAdapter.getConceptByUuid(DEFAULT_CONFIG_NAME, concept.getUuid());
        } catch (OpenMRSException e) {
            isOpenMRSExceptionThrown = Boolean.TRUE;
        }

        assertTrue(isOpenMRSExceptionThrown);
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean deleted = false;
        private boolean updated = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {

            if (event.getSubject().equals(EventKeys.CREATED_NEW_CONCEPT_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.UPDATED_CONCEPT_SUBJECT)) {
                updated = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_CONCEPT_SUBJECT)) {
                deleted = true;
            }
            eventParameters = event.getParameters();
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public String getIdentifier() {
            return "mrsTestListener";
        }
    }
}
