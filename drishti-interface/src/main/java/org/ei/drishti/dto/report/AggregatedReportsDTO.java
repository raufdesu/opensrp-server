package org.ei.drishti.dto.report;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ei.drishti.dto.LocationDTO;

import java.util.Map;

public class AggregatedReportsDTO {
    @JsonProperty
    private Map<String, Integer> ind;
    @JsonProperty
    private LocationDTO loc;

    public AggregatedReportsDTO(Map<String, Integer> indicatorSummary, LocationDTO loc) {
        this.ind = indicatorSummary;
        this.loc = loc;
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
