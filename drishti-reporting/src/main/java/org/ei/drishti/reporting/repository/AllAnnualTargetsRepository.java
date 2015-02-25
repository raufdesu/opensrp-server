package org.ei.drishti.reporting.repository;

import org.ei.drishti.reporting.domain.AnnualTarget;
import org.ei.drishti.reporting.domain.Indicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static org.ei.drishti.reporting.domain.AnnualTarget.FIND_BY_ANM_AND_INDICATOR_AND_DATE;

@Repository
public class AllAnnualTargetsRepository {
    private DataAccessTemplate dataAccessTemplate;

    protected AllAnnualTargetsRepository() {
    }

    @Autowired
    public AllAnnualTargetsRepository(@Qualifier("anmReportsDataAccessTemplate") DataAccessTemplate dataAccessTemplate) {
        this.dataAccessTemplate = dataAccessTemplate;
    }

    public AnnualTarget fetchFor(String anmIdentifier, Indicator indicator, Date reportDate) {
        return (AnnualTarget) dataAccessTemplate.getUniqueResult(FIND_BY_ANM_AND_INDICATOR_AND_DATE,
                new String[]{"anmIdentifier", "indicator", "reportDate"}, new Object[]{anmIdentifier, indicator.indicator(), reportDate});
    }
}
