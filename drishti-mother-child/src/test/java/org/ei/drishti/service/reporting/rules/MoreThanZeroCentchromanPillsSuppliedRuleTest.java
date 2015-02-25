package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoreThanZeroCentchromanPillsSuppliedRuleTest {

    private MoreThanZeroCentchromanPillsSuppliedRule rule;

    @Before
    public void setUp() throws Exception {
        rule = new MoreThanZeroCentchromanPillsSuppliedRule();
    }

    @Test
    public void shouldReturnFalseWhenNoCentchromanPillsAreSupplied() throws Exception {
        assertFalse(rule.apply(new SafeMap().put("numberOfCentchromanPillsDelivered", null)));

        assertFalse(rule.apply(new SafeMap().put("numberOfCentchromanPillsDelivered", "")));
    }

    @Test
    public void shouldReturnFalseWhenZeroCentchromanPillsAreSupplied() throws Exception {
        assertFalse(rule.apply(new SafeMap().put("numberOfCentchromanPillsDelivered", "0")));
    }

    @Test
    public void shouldReturnTrueWhenMoreThanZeroCentchromanPillsAreSupplied() throws Exception {
        assertTrue(rule.apply(new SafeMap().put("numberOfCentchromanPillsDelivered", "1")));
    }
}
