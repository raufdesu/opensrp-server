package org.ei.drishti.service.reporting.rules;

import junit.framework.Assert;
import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class IsChildLessThan23MonthsRuleTest {
    private IsChildLessThan23MonthsRule rule;

    @Before
    public void setUp() {
        rule = new IsChildLessThan23MonthsRule();
    }

    @Test
    public void shouldReturnTrueIfAgeIsLessThan23Months() {
        SafeMap safeMap = new SafeMap();
        safeMap.put("serviceProvidedDate", "2012-03-01");
        safeMap.put("dateOfBirth", "2012-01-01");

        boolean didRuleApply = rule.apply(safeMap);

        Assert.assertTrue(didRuleApply);
    }

    @Test
    public void shouldReturnFalseIfAgeIsNotLessThan23Months() {
        SafeMap safeMap = new SafeMap();
        safeMap.put("serviceProvidedDate", "2014-03-01");
        safeMap.put("dateOfBirth", "2011-01-01");

        boolean didRuleApply = rule.apply(safeMap);

        assertFalse(didRuleApply);
    }
}
