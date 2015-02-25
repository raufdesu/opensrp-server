package org.ei.drishti.service.scheduling.fpMethodStrategy;

import org.ei.drishti.domain.FPProductInformation;
import org.ei.drishti.service.ActionService;
import org.ei.drishti.service.scheduling.ScheduleService;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;

import static java.util.Arrays.asList;
import static org.ei.drishti.common.util.DateUtil.fakeIt;
import static org.ei.drishti.dto.AlertStatus.upcoming;
import static org.ei.drishti.dto.BeneficiaryType.ec;
import static org.joda.time.LocalDate.parse;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CondomStrategyTest {
    @Mock
    private ScheduleTrackingService scheduleTrackingService;
    @Mock
    private ActionService actionService;
    @Mock
    private ScheduleService scheduleService;

    private CondomStrategy strategy;
    private static final int preferredTime = 14;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        strategy = new CondomStrategy(scheduleTrackingService, actionService, scheduleService, preferredTime);
    }

    @Test
    public void shouldEnrollInCondomScheduleOnECRegistration() throws Exception {
        fakeIt(parse("2012-01-15"));
        strategy.registerEC(new FPProductInformation("entity id 1", "anm id 1", "condom", null, "2012-01-15", null, null
                , "20", "2012-03-01", null, null, null, null));

        verify(scheduleService).enroll("entity id 1", "Condom Refill", "2012-02-01");
        verify(actionService).alertForBeneficiary(ec, "entity id 1", "Condom Refill", "Condom Refill", upcoming, dateTime("2012-02-01"), dateTime("2012-02-08"));

        fakeIt(parse("2012-12-01"));
        strategy.registerEC(new FPProductInformation("entity id 1", "anm id 1", "condom", null, "2012-12-01", null, null
                , "20", "2012-03-01", null, null, null, null));

        verify(scheduleService).enroll("entity id 1", "Condom Refill", "2013-01-01");
        verify(actionService).alertForBeneficiary(ec, "entity id 1", "Condom Refill", "Condom Refill", upcoming, dateTime("2013-01-01"), dateTime("2013-01-08"));
    }

    @Test
    public void shouldUnEnrollECFromPreviousRefillSchedule() {
        strategy.unEnrollFromPreviousScheduleAsFPMethodChanged(new FPProductInformation("entity id 1", "anm id 1", "ocp", "condom", null, "1", null, null, null, "2012-01-01", null, null, null));

        verify(scheduleTrackingService).unenroll("entity id 1", asList("Condom Refill"));
        verify(actionService).markAlertAsClosed("entity id 1", "anm id 1", "Condom Refill", "2012-01-01");
    }

    @Test
    public void shouldEnrollECIntoCondomRefillScheduleWhenFPMethodIsChanged() {
        fakeIt(parse("2012-01-15"));

        strategy.enrollToNewScheduleForNewFPMethod(new FPProductInformation("entity id 1", "anm id 1", "condom", "ocp", null, null, null, null, null, "2012-01-01", null, null, null));

        verify(scheduleService).enroll("entity id 1", "Condom Refill", "2012-02-01");
        verify(actionService).alertForBeneficiary(ec, "entity id 1", "Condom Refill", "Condom Refill", upcoming, dateTime("2012-02-01"), dateTime("2012-02-08"));
    }

    @Test
    public void shouldUpdateECFromCondomRefillScheduleWhenCondomsAreResupplied() {
        fakeIt(parse("2011-01-15"));

        strategy.renewFPProduct(new FPProductInformation("entity id 1", "anm id 1", "condom", null, null, null, null, "20", "2011-01-12", "", null, null, null));

        InOrder inOrder = inOrder(scheduleTrackingService, scheduleService, actionService);
        inOrder.verify(scheduleTrackingService).fulfillCurrentMilestone("entity id 1", "Condom Refill", parse("2011-01-12"));
        inOrder.verify(actionService).markAlertAsClosed("entity id 1", "anm id 1", "Condom Refill", "2011-01-12");
        inOrder.verify(scheduleService).enroll("entity id 1", "Condom Refill", "2011-02-01");
        inOrder.verify(actionService).alertForBeneficiary(ec, "entity id 1", "Condom Refill", "Condom Refill", upcoming, dateTime("2011-02-01"), dateTime("2011-02-08"));
    }

    @Test
    public void shouldDoNothingWhenCondomsAreNotResupplied() {
        strategy.renewFPProduct(new FPProductInformation("entity id 1", "anm id 1", "condom", null, null, null, null, "", "2011-01-012", "", null, null, null));

        verifyZeroInteractions(scheduleTrackingService);
        verifyZeroInteractions(actionService);
    }

    private DateTime dateTime(String date) {
        return parse(date).toDateTime(new LocalTime(preferredTime, 0));
    }
}
