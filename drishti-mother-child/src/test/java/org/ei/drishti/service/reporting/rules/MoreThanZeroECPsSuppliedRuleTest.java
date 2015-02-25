package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoreThanZeroECPsSuppliedRuleTest {

    private MoreThanZeroECPsSuppliedRule rule;

    @Before
    public void setUp() throws Exception {
        rule = new MoreThanZeroECPsSuppliedRule();
    }

    @Test
    public void shouldReturnFalseWhenNoECPsAreSupplied() throws Exception {
        assertFalse(rule.apply(new SafeMap().put("numberOfECPsGiven", null)));

        assertFalse(rule.apply(new SafeMap().put("numberOfECPsGiven", "")));
    }

    @Test
    public void shouldReturnFalseWhenZeroECPsAreSupplied() throws Exception {
        assertFalse(rule.apply(new SafeMap().put("numberOfECPsGiven", "0")));
    }

    @Test
    public void shouldReturnTrueWhenMoreThanZeroECPsAreSupplied() throws Exception {
        assertTrue(rule.apply(new SafeMap().put("numberOfECPsGiven", "1")));
    }
}
