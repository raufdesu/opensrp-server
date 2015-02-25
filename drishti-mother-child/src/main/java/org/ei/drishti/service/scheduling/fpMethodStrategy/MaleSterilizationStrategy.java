package org.ei.drishti.service.scheduling.fpMethodStrategy;

import org.ei.drishti.contract.Schedule;
import org.ei.drishti.domain.FPProductInformation;
import org.ei.drishti.service.ActionService;
import org.ei.drishti.service.scheduling.ScheduleService;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.ei.drishti.scheduler.DrishtiScheduleConstants.ECSchedulesConstants.*;
import static org.joda.time.LocalDate.parse;

@Component
public class MaleSterilizationStrategy implements FPMethodStrategy {
    private static Logger logger = LoggerFactory.getLogger(MaleSterilizationStrategy.class.toString());
    private final ScheduleTrackingService scheduleTrackingService;
    private final ActionService actionService;
    private final ScheduleService scheduleService;
    private final Schedule maleSterilizationFollowupSchedule = new Schedule(EC_SCHEDULE_MALE_STERILIZATION_FOLLOWUP,
            asList(EC_SCHEDULE_MALE_STERILIZATION_FOLLOWUP_MILESTONE_1,
                    EC_SCHEDULE_MALE_STERILIZATION_FOLLOWUP_MILESTONE_2));

    @Autowired
    public MaleSterilizationStrategy(ScheduleTrackingService scheduleTrackingService, ActionService actionService,
                                     ScheduleService scheduleService) {
        this.scheduleTrackingService = scheduleTrackingService;
        this.actionService = actionService;
        this.scheduleService = scheduleService;
    }

    @Override
    public void registerEC(FPProductInformation fpInfo) {
        enrollECToMaleSterilizationSchedule(fpInfo.entityId(), fpInfo.fpMethodChangeDate());
    }

    @Override
    public void unEnrollFromPreviousScheduleAsFPMethodChanged(FPProductInformation fpInfo) {
        logger.info(format("Un-enrolling EC from Male sterilization Followup schedule as FP method changed. entityId: {0}, new fp method: {1}",
                fpInfo.entityId(), fpInfo.currentFPMethod()));
        unEnrollECFromMaleSterilizationSchedule(fpInfo.entityId(), fpInfo.anmId(), fpInfo.fpMethodChangeDate());
    }

    @Override
    public void enrollToNewScheduleForNewFPMethod(FPProductInformation fpInfo) {
        enrollECToMaleSterilizationSchedule(fpInfo.entityId(), fpInfo.fpMethodChangeDate());
    }

    @Override
    public void renewFPProduct(FPProductInformation fpInfo) {
    }

    @Override
    public void fpFollowup(FPProductInformation fpInfo) {
        String currentMilestone = getCurrentMilestone(fpInfo);
        logger.info(format("Fulfilling current milestone For Male sterilization Followup schedule. entityId: {0}, Ref date: {1}, currentMilestone: {2}",
                fpInfo.entityId(), fpInfo.submissionDate(), currentMilestone));
        scheduleTrackingService.fulfillCurrentMilestone(fpInfo.entityId(), maleSterilizationFollowupSchedule.name(), parse(fpInfo.fpFollowupDate()));
        actionService.markAlertAsClosed(fpInfo.entityId(), fpInfo.anmId(), currentMilestone, fpInfo.fpFollowupDate());
    }

    private void enrollECToMaleSterilizationSchedule(String entityId, String referenceDate) {
        logger.info(format("Enrolling EC to Male sterilization Followup schedule. entityId: {0}, Ref date: {1}", entityId, referenceDate));
        scheduleService.enroll(entityId, maleSterilizationFollowupSchedule.name(), referenceDate);
    }

    private void unEnrollECFromMaleSterilizationSchedule(String entityId, String anmId, String submissionDate) {
        scheduleTrackingService.unenroll(entityId, asList(maleSterilizationFollowupSchedule.name()));
        actionService.markAlertAsClosed(entityId, anmId, maleSterilizationFollowupSchedule.name(), submissionDate);
    }

    private String getCurrentMilestone(FPProductInformation fpInfo) {
        return scheduleTrackingService.getEnrollment(fpInfo.entityId(), maleSterilizationFollowupSchedule.name()).getCurrentMilestoneName();
    }
}
