package org.ei.drishti.scheduler.router;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.scheduletracking.api.domain.MilestoneAlert;

import java.util.HashMap;
import java.util.Map;

import static org.ei.drishti.scheduler.router.Matcher.any;
import static org.ei.drishti.scheduler.router.Matcher.eq;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.scheduletracking.api.events.constants.EventDataKeys.*;

public class AlertRouterTest {
    @Mock
    private Action firstAction;

    @Mock
    private Action secondAction;

    @Mock
    private Action thirdAction;

    private AlertRouter router;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        router = new AlertRouter();
    }

    @Test
    public void shouldBeAbleToSetACatchAllRoute() {
        router.addRoute(any(), any(), any(), firstAction);

        assertRouteMatches("someSchedule", "someMilestone", "someWindow", firstAction, new HashMap<String, String>());
        assertRouteMatches("someOtherSchedule", "someOtherMilestone", "someOtherWindow", firstAction, new HashMap<String, String>());
    }

    @Test
    public void shouldBeAbleToSetARouteWhichMatchesBasedOnScheduleNameOnly() {
        router.addRoute(eq("Schedule X"), any(), any(), firstAction);

        assertNoRoutesMatch("someSchedule", "someMilestone", "someWindow");
        assertRouteMatches("Schedule X", "someMilestone", "someWindow", firstAction, new HashMap<String, String>());
    }

    @Test
    public void shouldBeAbleToSetARouteWhichMatchesBasedOnMilestoneNameOnly() {
        router.addRoute(any(), eq("Milestone X"), any(), firstAction);

        assertNoRoutesMatch("someSchedule", "someMilestone", "someWindow");
        assertRouteMatches("someOtherSchedule", "Milestone X", "someWindow", firstAction, new HashMap<String, String>());
    }

    @Test
    public void shouldBeAbleToSetARouteWhichMatchesBasedOnWindowNameOnly() {
        router.addRoute(any(), any(), eq("Window X"), firstAction);

        assertNoRoutesMatch("someSchedule", "someMilestone", "someWindow");
        assertRouteMatches("someOtherSchedule", "someOtherMilestone", "Window X", firstAction, new HashMap<String, String>());
    }

    @Test
    public void shouldBeAbleToSetARouteWhichMatchesBasedOnACombinationOfMatchers() {
        router.addRoute(eq("Milestone X"), any(), eq("Window X"), firstAction);

        assertNoRoutesMatch("someSchedule", "someMilestone", "someWindow");
        assertNoRoutesMatch("Milestone X", "someMilestone", "someWindow");
        assertNoRoutesMatch("someSchedule", "someMilestone", "Window X");
        assertRouteMatches("Milestone X", "someOtherMilestone", "Window X", firstAction, new HashMap<String, String>());
    }

    @Test
    public void shouldBeAbleToSetARouteWithExtraData() {
        router.addRoute(eq("Milestone X"), any(), eq("Window X"), firstAction).addExtraData("Unicorns", "AreFun");

        assertNoRoutesMatch("someSchedule", "someMilestone", "someWindow");
        assertNoRoutesMatch("Milestone X", "someMilestone", "someWindow");
        assertNoRoutesMatch("someSchedule", "someMilestone", "Window X");

        HashMap<String, String> extraData = new HashMap<>();
        extraData.put("Unicorns", "AreFun");
        assertRouteMatches("Milestone X", "someOtherMilestone", "Window X", firstAction, extraData);
    }

    @Test
    public void shouldInvokeTheActionOfOnlyTheFirstRouteWhichMatchesInTheOrderInWhichTheyWereAdded() {
        router.addRoute(any(), any(), eq("Window X"), firstAction);
        router.addRoute(any(), any(), any(), secondAction);

        assertRouteMatches("someSchedule", "someMilestone", "Window X", firstAction, new HashMap<String, String>());
        verifyZeroInteractions(secondAction);

        assertRouteMatches("someSchedule", "someMilestone", "someOtherWindow", secondAction, new HashMap<String, String>());
        verifyZeroInteractions(firstAction);
    }

    @Test
    public void shouldNotTryAndLookAtTheBestMatchButJustTheFirstProperMatch() {
        router.addRoute(any(), any(), eq("Window X"), firstAction);
        router.addRoute(eq("Milestone X"), any(), eq("Window X"), secondAction);

        assertRouteMatches("Milestone X", "someMilestone", "Window X", firstAction, new HashMap<String, String>());
        verifyZeroInteractions(secondAction);
    }

    @Test(expected = NoRoutesMatchException.class)
    public void shouldFailIfNoRoutesMatch() {
        router.handle(event("scheduleName", "milestoneName", "windowName"));
    }

    private void assertRouteMatches(String schedule, String milestone, String window, Action action, HashMap<String, String> extraData) {
        MotechEvent event = handleEvent(schedule, milestone, window);
        verify(action, times(1)).invoke(new MilestoneEvent(event), extraData);
    }

    private void assertNoRoutesMatch(String schedule, String milestone, String window) {
        try {
            handleEvent(schedule, milestone, window);
            fail("Expected no routes to match! It matched a route.");
        } catch (NoRoutesMatchException e) {
        }
    }

    private MotechEvent handleEvent(String schedule, String milestone, String window) {
        MotechEvent event = event(schedule, milestone, window);
        router.handle(event);
        return event;
    }

    private MotechEvent event(String scheduleName, String milestoneName, String windowName) {
        MilestoneAlert alert = mock(MilestoneAlert.class);
        when(alert.getMilestoneName()).thenReturn(milestoneName);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(SCHEDULE_NAME, scheduleName);
        parameters.put(MILESTONE_NAME, alert);
        parameters.put(WINDOW_NAME, windowName);

        return new MotechEvent("Subject", parameters);
    }
}
