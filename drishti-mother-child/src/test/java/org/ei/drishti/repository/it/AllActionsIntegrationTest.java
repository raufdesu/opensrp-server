package org.ei.drishti.repository.it;

import org.ei.drishti.domain.Action;
import org.ei.drishti.dto.ActionData;
import org.ei.drishti.repository.AllActions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.ei.drishti.dto.AlertStatus.normal;
import static org.ei.drishti.dto.BeneficiaryType.mother;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-drishti.xml")
public class AllActionsIntegrationTest {
    @Autowired
    AllActions allActions;

    @Before
    public void setUp() throws Exception {
        allActions.removeAll();
    }

    @Test
    public void shouldSaveAReminder() throws Exception {
        Action alertAction = new Action("Case X", "ANM phone no", alert());

        allActions.add(alertAction);

        List<Action> allTheAlertActionsInDB = allActions.getAll();
        assertEquals(1, allTheAlertActionsInDB.size());
        assertEquals(alertAction, allTheAlertActionsInDB.get(0));
    }

    @Test
    public void shouldNotFindAnyAlertsIfNoneExistForGivenANM() throws Exception {
        assertEquals(0, allActions.findByANMIDAndTimeStamp("ANM 1", 0).size());
    }

    @Test
    public void shouldReturnAlertActionsBasedOnANMIDAndTimeStamp() throws Exception {
        Action firstAction = new Action("Case X", "ANM 1", alert());
        allActions.add(firstAction);

        Action secondAction = new Action("Case Y", "ANM 1", alert());
        allActions.add(secondAction);

        Action thirdAction = new Action("Case Z", "ANM 1", alert());
        allActions.add(thirdAction);

        assertEquals(asList(firstAction, secondAction, thirdAction), allActions.findByANMIDAndTimeStamp("ANM 1", 0));
        assertEquals(asList(secondAction, thirdAction), allActions.findByANMIDAndTimeStamp("ANM 1", firstAction.timestamp()));
        assertEquals(asList(thirdAction), allActions.findByANMIDAndTimeStamp("ANM 1", secondAction.timestamp()));

        assertEquals(0, allActions.findByANMIDAndTimeStamp("ANM 1", thirdAction.timestamp()).size());
    }

    @Test
    public void shouldFindAlertsOnlyForTheANMSpecified() throws Exception {
        Action firstAction = new Action("Case X", "ANM 1", alert());
        allActions.add(firstAction);

        Action secondAction = new Action("Case Y", "ANM 2", alert());
        allActions.add(secondAction);

        assertEquals(asList(firstAction), allActions.findByANMIDAndTimeStamp("ANM 1", 0));
        assertEquals(asList(secondAction), allActions.findByANMIDAndTimeStamp("ANM 2", 0));
    }

    @Test
    public void shouldMarkAllActionsAsInActiveForACase() throws Exception {
        Action firstAction = new Action("Case X", "ANM 1", alert());
        Action secondAction = new Action("Case X", "ANM 1", alert());
        Action thirdAction = new Action("Case Y", "ANM 2", alert());
        allActions.add(firstAction);
        allActions.add(secondAction);
        allActions.add(thirdAction);

        allActions.markAllAsInActiveFor("Case X");

        assertEquals(asList(firstAction.markAsInActive(), secondAction.markAsInActive()), allActions.findByANMIDAndTimeStamp("ANM 1", 0));
        assertEquals(asList(thirdAction), allActions.findByANMIDAndTimeStamp("ANM 2", 0));
    }

    @Test
    public void shouldFetchAlertsSortedByTimestamp() throws Exception {
        Action earlierAction = new Action("Case X", "ANM 1", alert());
        Thread.sleep(100);
        Action laterAction = new Action("Case X", "ANM 1", alert());
        Thread.sleep(100);
        Action latestAction = new Action("Case X", "ANM 1", alert());

        allActions.add(laterAction);
        allActions.add(latestAction);
        allActions.add(earlierAction);

        assertEquals(asList(earlierAction, laterAction, latestAction), allActions.findByANMIDAndTimeStamp("ANM 1", 0));
    }

    @Test
    public void shouldNotDoAnythingIfNoActionsAreFoundForATarget() {
        Action alertAction = new Action("Case X", "ANM 1", alert());
        allActions.add(alertAction);

        allActions.deleteAllByTarget("report");

        assertEquals(asList(alertAction), allActions.findByANMIDAndTimeStamp("ANM 1", 0));
    }

    @Test
    public void shouldReturnActionBasedOnANMIdEntityIdScheduleName() throws Exception {
        Action anmAction = new Action("entity id 1", "anm id 1", alert("schedule1", "milestone1"));
        Action anotherANMAction = new Action("entity id 2", "anm id 2", alert("schedule2", "milestone2"));
        Action anotherEntityAction = new Action("entity id 2", "anm id 1", alert("schedule1", "milestone1"));
        Action anotherScheduleAction = new Action("entity id 1", "anm id 1", alert("schedule2", "milestone1"));
        allActions.add(anmAction);
        allActions.add(anotherEntityAction);
        allActions.add(anotherANMAction);
        allActions.add(anotherScheduleAction);

        assertEquals(asList(anmAction), allActions.findAlertByANMIdEntityIdScheduleName("anm id 1", "entity id 1", "schedule1"));
        assertEquals(asList(anotherScheduleAction), allActions.findAlertByANMIdEntityIdScheduleName("anm id 1", "entity id 1", "schedule2"));
        assertEquals(asList(anotherEntityAction), allActions.findAlertByANMIdEntityIdScheduleName("anm id 1", "entity id 2", "schedule1"));
        assertEquals(asList(anotherANMAction), allActions.findAlertByANMIdEntityIdScheduleName("anm id 2", "entity id 2", "schedule2"));
    }

    @Test
    public void shouldRemoveExistingAlertBeforeAddingNewOne() throws Exception {
        Action existingAlert = new Action("entity id 1", "anm id 1", alert("schedule1", "milestone1"));
        Action existingDifferentScheduleAlert = new Action("entity id 1", "anm id 1", alert("schedule2", "milestone3"));
        allActions.add(existingAlert);
        allActions.add(existingDifferentScheduleAlert);

        Action newAlert = new Action("entity id 1", "anm id 1", alert("schedule1", "milestone2"));
        allActions.addOrUpdateAlert(newAlert);

        assertFalse(allActions.contains(existingAlert.getId()));
        assertTrue(allActions.contains(existingDifferentScheduleAlert.getId()));
        assertEquals(allActions.findAlertByANMIdEntityIdScheduleName("anm id 1", "entity id 1", "schedule1").get(0).getId(), newAlert.getId());
    }

    @Test
    public void shouldAddNewAlertWhenThereIsNoExistingAlert() throws Exception {
        Action newAlert = new Action("entity id 1", "anm id 1", alert("schedule1", "milestone2"));
        allActions.addOrUpdateAlert(newAlert);

        assertTrue(allActions.contains(newAlert.getId()));
    }

    @Test
    public void shouldUpdateAlertAsAnInactive() {
        Action firstAction = new Action("Case X", "ANM 1", alert("schedule1", "milestone1"));
        Action secondAction = new Action("Case X", "ANM 1", alert("schedule2", "milestone2"));
        allActions.add(firstAction);
        allActions.add(secondAction);

        allActions.markAlertAsInactiveFor("ANM 1", "Case X", "schedule1");

        assertEquals(asList(firstAction.markAsInActive()), allActions.findAlertByANMIdEntityIdScheduleName("ANM 1", "Case X", "schedule1"));
        assertEquals(asList(secondAction), allActions.findAlertByANMIdEntityIdScheduleName("ANM 1", "Case X", "schedule2"));
    }

    private ActionData alert() {
        return ActionData.createAlert(mother, "Ante Natal Care - Normal", "ANC 1", normal, DateTime.now(), DateTime.now().plusDays(3));
    }

    private ActionData alert(String schedule, String milestone) {
        return ActionData.createAlert(mother, schedule, milestone, normal, DateTime.now(), DateTime.now().plusDays(3));
    }
}
