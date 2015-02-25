package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.HEAVY_BLEEDING_VALUE;
import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.VAGINAL_PROBLEMS_FIELD_NAME;

@Component
public class IsMotherDoesNotHaveHemorrhageRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {

        String vaginalProblemValue = reportFields.get(VAGINAL_PROBLEMS_FIELD_NAME) != null ?
                reportFields.get(VAGINAL_PROBLEMS_FIELD_NAME) : "";

        return !vaginalProblemValue.contains(HEAVY_BLEEDING_VALUE);
    }
}
