package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class IsSideEffectDueToSterilizationRuleTest {

    IsSideEffectDueToSterilizationRule rule;

    @Before
    public void setUp() {
        rule = new IsSideEffectDueToSterilizationRule();
    }

    @Test
    public void shouldReturnFalseWhenThereIsNoSideEffectDueToSterilization() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("sterilizationSideEffect", null));

        assertFalse(didRuleSucceed);

        didRuleSucceed = rule.apply(new SafeMap().put("sterilizationSideEffect", ""));

        assertFalse(didRuleSucceed);
    }

    @Test
    public void shouldReturnTrueWhenThereIsSideEffectDueToSterilization() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("sterilizationSideEffect", "headache"));

        assertTrue(didRuleSucceed);
    }
}
