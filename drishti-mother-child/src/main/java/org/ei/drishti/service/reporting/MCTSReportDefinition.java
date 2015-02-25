package org.ei.drishti.service.reporting;

import ch.lambdaj.util.NotUniqueItemException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;

public class MCTSReportDefinition {
    private static Logger logger = LoggerFactory.getLogger(MCTSReportDefinition.class.toString());

    private List<MCTSFormIndicator> formIndicators;

    public MCTSReportDefinition(List<MCTSFormIndicator> formIndicators) {
        this.formIndicators = formIndicators;
    }

    public List<MCTSReportIndicator> getIndicatorsByFormName(String formName) {
        try {
            MCTSFormIndicator formIndicator = selectUnique(formIndicators,
                    having(on(MCTSFormIndicator.class).form(), equalTo(formName)));
            return formIndicator == null ? new ArrayList<MCTSReportIndicator>() : formIndicator.indicators();
        } catch (NotUniqueItemException e) {
            logger.error("There are more than one form indicator definition for the form: " + formName);
            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}