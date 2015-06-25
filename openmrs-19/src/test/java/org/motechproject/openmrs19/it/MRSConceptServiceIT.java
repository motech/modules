package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    OpenMRSConcept conceptOne;
    OpenMRSConcept conceptTwo;

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
        assertEquals(conceptOne.getDataType(), mrsListener.eventParameters.get(EventKeys.CONCEPT_DATA_TYPE));
        assertEquals(conceptOne.getConceptClass(), mrsListener.eventParameters.get(EventKeys.CONCEPT_CONCEPT_CLASS));

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldUpdateConcept() throws InterruptedException, ConceptNameAlreadyInUseException {

        conceptOne.setDataType("Text");
        conceptOne.setConceptClass("Test");

        OpenMRSConcept updatedConcept;

        synchronized (lock) {
            updatedConcept = (OpenMRSConcept) conceptAdapter.updateConcept(conceptOne);
            lock.wait(6000);
        }

        assertNotNull(updatedConcept);

        assertEquals(conceptOne.getDataType(), mrsListener.eventParameters.get(EventKeys.CONCEPT_DATA_TYPE));
        assertEquals(conceptOne.getConceptClass(), mrsListener.eventParameters.get(EventKeys.CONCEPT_CONCEPT_CLASS));

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteConcept() throws InterruptedException, ConceptNameAlreadyInUseException {

        synchronized (lock) {
            conceptAdapter.deleteConcept(conceptOne.getUuid());
            assertNull(conceptAdapter.getConceptByUuid(conceptOne.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetAllConcepts() throws ConceptNameAlreadyInUseException, InterruptedException {

        List<? extends OpenMRSConcept> concepts = conceptAdapter.getAllConcepts();

        assertTrue(concepts.size() >= 2);
    }

    @Test
    public void shouldGetConcepts() {

        List<? extends OpenMRSConcept> concepts = conceptAdapter.getConcepts(1, 1);

        assertTrue(concepts.size() == 1);

        concepts = conceptAdapter.getConcepts(1, 2);

        assertTrue(concepts.size() == 2);

    }

    @Test
    public void shouldFindSingleConceptByName() throws InterruptedException, ConceptNameAlreadyInUseException {

        List<? extends OpenMRSConcept> concepts = conceptAdapter.search(conceptOne.getName().getName());

        assertEquals(1, concepts.size());

        assertEquals(concepts.get(0), conceptOne);

    }

    @After
    public void tearDown() throws InterruptedException {

        deleteConcept(conceptOne);
        deleteConcept(conceptTwo);

        eventListenerRegistry.clearListenersForBean(mrsListener.getIdentifier());
    }

    private OpenMRSConcept prepareConceptOne() {

        OpenMRSConcept concept = new OpenMRSConcept();
        OpenMRSConceptName conceptName = new OpenMRSConceptName("FooConceptOne");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDataType("TEXT");
        concept.setConceptClass("Test");

        return concept;
    }

    private OpenMRSConcept prepareConceptTwo() {

        OpenMRSConcept concept = new OpenMRSConcept();
        OpenMRSConceptName conceptName = new OpenMRSConceptName("FooConceptTwo");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDataType("TEXT");
        concept.setConceptClass("Test");

        return concept;
    }

    private OpenMRSConcept createConcept(OpenMRSConcept concept) throws ConceptNameAlreadyInUseException, InterruptedException {

        OpenMRSConcept created;

        synchronized (lock) {
            created = conceptAdapter.createConcept(concept);
            assertNotNull(created);
            lock.wait(60000);
        }
        return created;
    }

    private void deleteConcept(OpenMRSConcept concept) throws InterruptedException {

        conceptAdapter.deleteConcept(concept.getUuid());
        assertNull(conceptAdapter.getConceptByUuid(concept.getUuid()));

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
