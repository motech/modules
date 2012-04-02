package org.motechproject.adherence.service;

import org.apache.commons.lang.StringUtils;
import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.adherence.domain.AdherenceToken;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/testAdherenceApplicationContext.xml"})
public class AdherenceTokenServiceIT extends BaseTest {

    @Autowired
    private AdherenceTokenService adherenceTokenService;
    @Autowired
    @Qualifier("adherenceDbConnector")
    private CouchDbConnector couchDbConnector;
    private String externalId = "externalId";
    private String referenceId = "referenceId";

    private List<MotechBaseDataObject> entities = new ArrayList<MotechBaseDataObject>();

    @Test
    public void shouldCreateNewToken() {
        LocalDate today = DateUtil.today();
        mockTime(DateUtil.newDateTime(today, 10, 0, 0));
        AdherenceToken adherenceToken = adherenceTokenService.createToken(externalId, referenceId);
        entities.add(adherenceToken);

        assertNotNull(adherenceToken);
        assertEquals(today, adherenceToken.getCreationDate());
        resetTime();
    }

    @Test
    public void shouldRetrieveLatestToken() {
        LocalDate today = DateUtil.today();
        mockTime(DateUtil.newDateTime(today, 10, 0, 0));
        AdherenceToken oldToken = adherenceTokenService.createToken(externalId, referenceId);
        mockTime(DateUtil.newDateTime(today.plusDays(1), 10, 0, 0));
        AdherenceToken newToken = adherenceTokenService.createToken(externalId, referenceId);
        entities.addAll(Arrays.asList(oldToken, newToken));
        assertEquals(newToken, adherenceTokenService.getLatestToken(externalId, referenceId));
    }

    @Test
    public void shouldRetrieveTokensBetweenTwoDates() {
        LocalDate today = DateUtil.today();
        mockTime(DateUtil.newDateTime(today.minusDays(2), 10, 0, 0));
        AdherenceToken oldToken = adherenceTokenService.createToken(externalId, referenceId);
        mockTime(DateUtil.newDateTime(today, 10, 0, 0));
        AdherenceToken requiredToken = adherenceTokenService.createToken(externalId, referenceId);
        mockTime(DateUtil.newDateTime(today.plusDays(2), 10, 0, 0));
        AdherenceToken newToken = adherenceTokenService.createToken(externalId, referenceId);
        entities.addAll(Arrays.asList(oldToken, requiredToken, newToken));
        assertEquals(requiredToken, adherenceTokenService.getTokensBetween(externalId, referenceId, today.minusDays(1), today.plusDays(1)).get(0));
    }

    @After
    public void tearDown() {
        for (MotechBaseDataObject entity : entities) {
            if (entity != null && StringUtils.isNotEmpty(entity.getId())) {
                try {
                    couchDbConnector.delete(entity);
                } catch (Exception e) {

                }
            }
        }
        entities.removeAll(entities);
    }

}
