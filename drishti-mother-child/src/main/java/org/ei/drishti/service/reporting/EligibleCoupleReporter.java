package org.ei.drishti.service.reporting;

import org.ei.drishti.common.domain.Indicator;
import org.ei.drishti.domain.EligibleCouple;
import org.ei.drishti.domain.Location;
import org.ei.drishti.repository.AllEligibleCouples;
import org.ei.drishti.util.SafeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.ei.drishti.common.AllConstants.CommonFormFields.SUBMISSION_DATE_FIELD_NAME;

@Component
public class EligibleCoupleReporter implements IReporter {

    private ECReportingService ecReportingService;
    private AllEligibleCouples allEligibleCouples;

    @Autowired
    public EligibleCoupleReporter(ECReportingService ecReportingService, AllEligibleCouples allEligibleCouples) {
        this.ecReportingService = ecReportingService;
        this.allEligibleCouples = allEligibleCouples;
    }

    @Override
    public void report(String entityId, String reportIndicator, Location location, String serviceProvidedDate, SafeMap reportData) {
        EligibleCouple eligibleCouple = allEligibleCouples.findByCaseId(entityId);
        //#TODO: Pull out the reportIndicator method out of ecReportingService
        ecReportingService.reportIndicator(reportData, eligibleCouple, Indicator.from(reportIndicator), serviceProvidedDate, reportData.get(SUBMISSION_DATE_FIELD_NAME));
    }

}
