package org.ei.drishti.web.controller;

import org.ei.drishti.dto.Action;
import org.ei.drishti.dto.ActionData;

public class ActionConvertor {
    public static Action from(org.ei.drishti.domain.Action action){
        return new Action(action.caseId(), action.target(), action.actionType(), action.data(), String.valueOf(action.timestamp()), action.getIsActionActive(), action.details());
    }

    public static org.ei.drishti.domain.Action toAction(Action actionItem, String anmIdentifier) {
        return new org.ei.drishti.domain.Action(actionItem.caseID(), anmIdentifier, ActionData.from(actionItem.type(), actionItem.target(), actionItem.data(), actionItem.details()));
    }
}
