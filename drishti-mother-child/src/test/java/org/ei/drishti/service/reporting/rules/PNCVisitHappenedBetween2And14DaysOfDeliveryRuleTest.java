package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.ei.drishti.common.util.EasyMap.create;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PNCVisitHappenedBetween2And14DaysOfDeliveryRuleTest {

    private PNCVisitHappenedBetween2And14DaysOfDeliveryRule rule;

    @Before
    public void setUp() throws Exception {
        rule = new PNCVisitHappenedBetween2And14DaysOfDeliveryRule();
    }

    @Test
    public void shouldReturnTrueWhenPNCVisitHappenedBetween2And14DaysOfDelivery() {
        Map<String, String> reportFields =
                create("pncVisitDate", "2013-01-04")
                        .put("referenceDate", "2013-01-01")
                        .map();
        boolean didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertTrue(didRuleSucceed);

        reportFields =
                create("pncVisitDate", "2013-01-15")
                        .put("referenceDate", "2013-01-01")
                        .map();
        didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertTrue(didRuleSucceed);

        reportFields =
                create("pncVisitDate", "2013-01-12")
                        .put("referenceDate", "2013-01-01")
                        .map();
        didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertTrue(didRuleSucceed);
    }

    @Test
    public void shouldReturnFalseWhenPNCVisitNotHappenedBetween2And14DaysOfDelivery() {
        Map<String, String> reportFields =
                create("pncVisitDate", "2013-01-03")
                        .put("referenceDate", "2013-01-01")
                        .map();
        boolean didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertFalse(didRuleSucceed);

        reportFields =
                create("pncVisitDate", "2013-01-02")
                        .put("referenceDate", "2013-01-01")
                        .map();
        didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertFalse(didRuleSucceed);

        reportFields =
                create("pncVisitDate", "2013-01-16")
                        .put("referenceDate", "2013-01-01")
                        .map();
        didRuleSucceed = rule.apply(new SafeMap(reportFields));
        assertFalse(didRuleSucceed);
    }
}