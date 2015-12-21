package org.motechproject.mtraining.service.ut.query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.mtraining.repository.query.UnusedUnitQueryExecution;

import javax.jdo.Query;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UnusedUnitQueryExecutionTest {

    @Mock
    private Query query;

    @Mock
    private InstanceSecurityRestriction instanceSecurityRestriction;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBuildCorrectFilter() {
        UnusedUnitQueryExecution queryExecution = new UnusedUnitQueryExecution("fieldName");
        queryExecution.execute(query, instanceSecurityRestriction);

        verify(query).setFilter("fieldName == null");
    }
}
