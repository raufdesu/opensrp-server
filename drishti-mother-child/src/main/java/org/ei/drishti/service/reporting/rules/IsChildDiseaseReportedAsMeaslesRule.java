package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.ChildIllnessFields.REPORT_CHILD_DISEASE;
import static org.ei.drishti.common.AllConstants.ChildImmunizationFields.MEASLES_VALUE;

@Component
public class IsChildDiseaseReportedAsMeaslesRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        return MEASLES_VALUE.equalsIgnoreCase(reportFields.get(REPORT_CHILD_DISEASE));
    }
}