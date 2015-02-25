package org.ei.drishti.repository;

import java.util.List;

import org.ei.drishti.common.AllConstants;
import org.ei.drishti.domain.Child;
import org.ei.drishti.domain.Bidan;
import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllBidans extends MotechBaseRepository<Bidan> {
    private static Logger logger = LoggerFactory.getLogger(AllEligibleCouples.class);
	@Autowired
    public AllBidans(@Qualifier(AllConstants.DRISHTI_DATABASE_CONNECTOR) CouchDbConnector db) {
        super(Bidan.class, db);
    }
	public Bidan findByCaseId(String caseId) {
		 List<Bidan> bidan = queryView("by_caseId", caseId);
	        if (bidan == null || bidan.isEmpty()) {
	            return null;
	        }
	        return bidan.get(0);
	}

}
