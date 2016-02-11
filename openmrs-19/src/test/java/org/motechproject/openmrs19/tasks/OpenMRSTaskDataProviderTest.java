package org.motechproject.openmrs19.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private ResourceLoader resourceLoader;

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        taskDataProvider = new OpenMRSTaskDataProvider(resourceLoader, providerService);
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        String className = "testClass";
        Object object = taskDataProvider.lookup(className, "lookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForProvider() {
        String className = OpenMRSProvider.class.getSimpleName();
        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetProviderByUuid() {
        String className = OpenMRSProvider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(providerService.getProviderByUuid(null)).thenReturn(null);
        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(null);
    }

    @Test
    public void shouldReturnNullWhenProviderNotFoundForLookupGetProviderByUuid() {
        String className = OpenMRSProvider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(providerService.getProviderByUuid("4")).thenReturn(null);
        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid("4");
    }

    @Test
    public void shouldReturnProviderForLookupGetProviderByUuid() {
        String className = OpenMRSProvider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        OpenMRSProvider openMRSProvider = new OpenMRSProvider();
        openMRSProvider.setIdentifier("testIdentifier");

        when(providerService.getProviderByUuid("5")).thenReturn(openMRSProvider);
        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertTrue(object instanceof OpenMRSProvider);
        assertEquals(((OpenMRSProvider) object).getIdentifier(), "testIdentifier");
        verify(providerService).getProviderByUuid("5");
    }
}
