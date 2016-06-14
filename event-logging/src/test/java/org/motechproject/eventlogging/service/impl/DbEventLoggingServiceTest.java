package org.motechproject.eventlogging.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.eventlogging.loggers.impl.DbEventLogger;
import org.motechproject.eventlogging.matchers.LogMappings;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.matchers.MappedLoggableEvent;
import org.motechproject.eventlogging.matchers.MappingsJson;
import org.motechproject.eventlogging.matchers.ParametersPresentEventFlag;
import org.motechproject.eventlogging.repository.AllEventMappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DbEventLoggingServiceTest {

    @Mock
    AllEventMappings allEventMappings;

    private DbEventLoggingService dbEventLoggingService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldSetAllLoggableEventsCorrectly() {

        List<MappingsJson> mappings = new ArrayList<MappingsJson>();

        MappingsJson mapping = new MappingsJson();
        MappingsJson mapping2 = new MappingsJson();

        List<String> mapSubjects = new ArrayList<String>();
        List<String> mapSubjects2 = new ArrayList<String>();

        mapSubjects.add("org.test");
        mapSubjects.add("*");

        mapSubjects2.add("org.*");
        mapSubjects2.add("test.*");
        mapSubjects2.add("org.test");

        mapping.setSubjects(mapSubjects);

        mapping2.setSubjects(mapSubjects2);

        List<ParametersPresentEventFlag> flags = new ArrayList<ParametersPresentEventFlag>();

        Map<String, String> keyValuePairsPresent = new HashMap<String, String>();

        keyValuePairsPresent.put("present1", "presentValue1");
        keyValuePairsPresent.put("present2", "presentValue2");

        ParametersPresentEventFlag flag1 = new ParametersPresentEventFlag(
                keyValuePairsPresent);

        flags.add(flag1);

        mapping.setFlags(flags);

        List<String> includes = new ArrayList<String>();

        includes.add("include1");
        includes.add("include2");

        List<String> excludes = new ArrayList<String>();

        excludes.add("exclude1");
        excludes.add("exclude2");

        mapping2.setExcludes(excludes);

        mapping2.setFlags(flags);

        List<Map<String, String>> eventMappings = new ArrayList<Map<String, String>>();

        Map<String, String> mapParameters = new LinkedHashMap<String, String>();

        mapParameters.put("start", "value1");
        mapParameters.put("end", "value2");

        Map<String, String> mapParameters2 = new LinkedHashMap<String, String>();

        mapParameters2.put("start2", "test1");
        mapParameters2.put("end2", "test2");

        Map<String, String> mapParameters3 = new LinkedHashMap<String, String>();

        mapParameters3.put("start3", "test3");
        mapParameters3.put("end3", "test4");

        eventMappings.add(mapParameters);
        eventMappings.add(mapParameters2);
        eventMappings.add(mapParameters3);

        mapping2.setMappings(eventMappings);

        mapping2.setIncludes(includes);

        mappings.add(mapping);

        mappings.add(mapping2);

        when(allEventMappings.getAllMappings()).thenReturn(mappings);

        dbEventLoggingService = new DbEventLoggingService(allEventMappings);

        DbEventLogger dbEventLogger = dbEventLoggingService.getDefaultDbEventLogger();

        assertNotNull(dbEventLogger);

        List<LoggableEvent> loggableEvents = dbEventLogger.getLoggableEvents();

        LoggableEvent event = loggableEvents.get(0);

        assertNotNull(event);

        assertEquals(event.getEventSubjects().size(), 2);
        assertEquals(event.getFlags().size(), 1);

        LoggableEvent event2 = loggableEvents.get(1);

        assertNotNull(event2);

        assertFalse(event.getClass().equals(MappedLoggableEvent.class));
        assertTrue(event.getClass().equals(LoggableEvent.class));

        assertTrue(event2.getClass().equals(MappedLoggableEvent.class));
        assertFalse(event2.getClass().equals(LoggableEvent.class));

        MappedLoggableEvent castedEvent = (MappedLoggableEvent) event2;

        LogMappings logMappings = castedEvent.getMappings();

        assertNotNull(logMappings);

        assertNotNull(logMappings.getExclusions());

        assertNotNull(logMappings.getInclusions());

        assertNotNull(logMappings.getMappings());

        assertEquals(logMappings.getMappings().size(), 3);

        assertEquals(logMappings.getInclusions().size(), 2);

        assertEquals(logMappings.getExclusions().size(), 2);

        assertEquals(castedEvent.getFlags().size(), 1);

        ParametersPresentEventFlag eventFlag = (ParametersPresentEventFlag) castedEvent
                .getFlags().get(0);

        Map<String, String> keyValuePairs = eventFlag.getKeyValuePairsPresent();
        assertEquals(keyValuePairs.size(), 2);
        assertEquals(keyValuePairs.get("present1"), "presentValue1");
        assertEquals(keyValuePairs.get("present2"), "presentValue2");

        Set<String> subjects = dbEventLoggingService
                .getLoggedEventSubjects();

        assertEquals(subjects.size(), 4);
    }
}
