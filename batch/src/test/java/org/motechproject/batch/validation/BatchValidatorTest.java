package org.motechproject.batch.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BatchValidatorTest {

    @InjectMocks
    BatchValidator batchValidator = new BatchValidator();

    private String jobName;
    private String cronExpression;
    private String date;

    @Before
    public void setUp() throws Exception {
        jobName = "Co-ordinator";
        cronExpression = "0 0 12 * * ?";
        date = "29/07/2015 01:10:05";
    }

    /**
     * Valid inputs scenario
     */
    @Test
    public void validateSchedulerInputsTest() {
        List<String> errors = batchValidator.validateSchedulerInputs(jobName, cronExpression);
        assertNotNull(errors);
        assertEquals(0, errors.size());

    }

    /**
     * Invalid scenario: with null argument value of <code>jobName</code> null
     */
    @Test
    public void validateSchedulerInputsWithNullJobName() {
        List<String> errors = batchValidator.validateSchedulerInputs(null, cronExpression);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
    }

    /**
     * Invalid scenario: with empty argument value of <code>jobName</code> ""
     */
    @Test
    public void validateSchedulerInputsWithEmptyJobName() {
        List<String> errors = batchValidator.validateSchedulerInputs("", cronExpression);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
    }

    /**
     * Invalid scenario: with null argument value of <code>jobName</code> and
     * invalid <code>cronExpression</code> any string
     */
    @Test
    public void validateSchedulerInputsWithNullJobNameAndInvalidCronExpression() {
        List<String> errors = batchValidator.validateSchedulerInputs(null, "0 0 3 A ?");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
        assertEquals("Job cron expression supplied is not valid",
                errors.get(1));
    }

    /**
     * Invalid scenario: with invalid <code>cronExpression</code> any string
     */
    @Test
    public void validateSchedulerInputsWithJobNameAndInvalidCronExpression() {
        cronExpression = "kafka 0 0 12 * * ? ";
        List<String> errors = batchValidator.validateSchedulerInputs(jobName, cronExpression);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Job cron expression supplied is not valid",
                errors.get(0));
    }

    /**
     * Valid inputs scenario
     */

    @Test
    public void validateOneTimeInputsTest() {
        List<String> errors = batchValidator.validateOneTimeInputs(jobName,
                date);
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    /**
     * Invalid scenario: with null argument value of <code>jobName</code> and
     * empty <code>date</code>
     */
    @Test
    public void validateOneTimeInputsWithNullJobName() {
        List<String> errors = batchValidator.validateOneTimeInputs(null, "");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
        assertEquals("Date must be provided.", errors.get(1));
    }

    /**
     * Invalid scenario: with empty argument value of <code>jobName</code> and
     * invalid <code>date</code>
     */
    @Test
    public void validateOneTimeInputsWithEmptyJobName() {
        List<String> errors = batchValidator.validateOneTimeInputs("", "invalid");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
        assertEquals("Date passed is invalid. Passed value: [invalid]", errors.get(1));
    }

    /**
     * Valid inputs scenario
     */
    @Test
    public void validateUpdateInputsTest() {
        List<String> errors = batchValidator.validateUpdateInputs(jobName);
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    /**
     * Invalid scenario: with null argument value of <code>jobName</code>
     */
    @Test
    public void validateUpdateInputsWithNullJobName() {
        List<String> errors = batchValidator.validateUpdateInputs(null);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
    }

    /**
     * Invalid scenario: with empty argument value of <code>jobName</code> ""
     */
    @Test
    public void validateUpdateInputsWithEmptyJobName() {
        List<String> errors = batchValidator.validateUpdateInputs("");
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Job name must be provided", errors.get(0));
    }
}
