package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CurrentFPMethodIsFemaleSterilizationRuleTest {

    CurrentFPMethodIsFemaleSterilizationRule rule;

    @Before
    public void setUp() {
        rule = new CurrentFPMethodIsFemaleSterilizationRule();
    }

    @Test
    public void shouldReturnFalseWhenCurrentFPMethodOfECIsNotFemaleSterilization() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("currentMethod", "ocp"));

        assertFalse(didRuleSucceed);
    }

    @Test
    public void shouldReturnTrueIfCurrentFPMethodOfTheECIsFemaleSterilization() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("currentMethod", "female_sterilization"));

        assertTrue(didRuleSucceed);
    }
}
