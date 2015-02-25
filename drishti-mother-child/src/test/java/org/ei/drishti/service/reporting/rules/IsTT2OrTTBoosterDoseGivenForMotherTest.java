package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class IsTT2OrTTBoosterDoseGivenForMotherTest {

    private IsTT2OrTTBoosterDoseGivenForMother rule;

    @Before
    public void setUp() {
        rule = new IsTT2OrTTBoosterDoseGivenForMother();
    }

    @Test
    public void shouldReturnTrueWhenTT2OrTTBoosterDoseIsGiven() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("ttDose", "tt2"));

        assertTrue(didRuleSucceed);

        didRuleSucceed = rule.apply(new SafeMap().put("ttDose", "ttbooster"));

        assertTrue(didRuleSucceed);
    }

    @Test
    public void shouldReturnFalseWhenTT2OrTTBoosterDoseIsNotGiven() {
        boolean didRuleSucceed = rule.apply(new SafeMap().put("ttDose", "tt1"));

        assertFalse(didRuleSucceed);
    }
}