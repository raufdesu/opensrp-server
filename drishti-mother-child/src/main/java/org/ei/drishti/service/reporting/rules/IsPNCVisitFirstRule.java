package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.VISIT_DATES_FIELD_NAME;
import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.VISIT_DATE_FIELD_NAME;

@Component
public class IsPNCVisitFirstRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        String pncVisitDate = reportFields.get(VISIT_DATE_FIELD_NAME);
        String pncVisitDates = reportFields.get(VISIT_DATES_FIELD_NAME);
        if(pncVisitDates != null){
            String[] pncVisitDatesList = pncVisitDates.split(" ");
            return (pncVisitDatesList.length == 1 && pncVisitDatesList[0].equalsIgnoreCase(pncVisitDate));
        }
        return false;
    }
}
