package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.CommonFormFields.REFERENCE_DATE;
import static org.ei.drishti.common.AllConstants.PNCVisitFormFields.VISIT_DATE_FIELD_NAME;

@Component
public class IsPNCVisit48HoursOfDeliveryRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        LocalDate pncVisitDate = LocalDate.parse(reportFields.get(VISIT_DATE_FIELD_NAME));
        LocalDate dateOfBirth = LocalDate.parse(reportFields.get(REFERENCE_DATE));

        LocalDate threeDaysAfterDateOfBirth = dateOfBirth.plusDays(3);
        
        return pncVisitDate.isBefore(threeDaysAfterDateOfBirth);
    }
}
