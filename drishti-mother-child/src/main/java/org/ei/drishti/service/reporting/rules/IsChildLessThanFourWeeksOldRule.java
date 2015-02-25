package org.ei.drishti.service.reporting.rules;

import org.ei.drishti.util.SafeMap;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.ChildRegistrationFormFields.DATE_OF_BIRTH;
import static org.ei.drishti.common.AllConstants.CommonFormFields.SERVICE_PROVIDED_DATE;

@Component
public class IsChildLessThanFourWeeksOldRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        LocalDate dateOfBirth = LocalDate.parse(reportFields.get(DATE_OF_BIRTH));
        String serviceProvidedDate = reportFields.get(SERVICE_PROVIDED_DATE);

        return serviceProvidedDate != null ? dateOfBirth.plusWeeks(4).isAfter(LocalDate.parse(serviceProvidedDate)) : false;
    }
}
