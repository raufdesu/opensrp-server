package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.ChildIllnessFields.REPORT_CHILD_DISEASE_DATE;
import static org.ei.drishti.common.AllConstants.ChildRegistrationFormFields.DATE_OF_BIRTH;

@Component
public class IsChildBelowFiveYearsRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        LocalDate dateOfBirth = LocalDate.parse(reportFields.get(DATE_OF_BIRTH));
        LocalDate reportDate = LocalDate.parse(reportFields.get(REPORT_CHILD_DISEASE_DATE));
        return dateOfBirth.plusYears(5).isAfter(reportDate);
    }
}