package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.BAD_SMELL_LOCHEA_VALUE;
import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.VAGINAL_PROBLEMS_FIELD_NAME;

@Component
public class IsMotherDoesNotHaveBadSmellLocheaRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        String vaginalProblemsValue = reportFields.get(VAGINAL_PROBLEMS_FIELD_NAME) != null
                ? reportFields.get(VAGINAL_PROBLEMS_FIELD_NAME) : "";

        return !vaginalProblemsValue.contains(BAD_SMELL_LOCHEA_VALUE);
    }
}
