package org.ei.drishti.service.formSubmission;

import org.ei.drishti.domain.MCTSReport;
import org.ei.drishti.repository.AllMCTSReports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

@Component
public class MCTSSMSReportService {
    private static Logger logger = LoggerFactory.getLogger(MCTSSMSReportService.class.toString());
    private AllMCTSReports mctsReportRepository;

    @Autowired
    public MCTSSMSReportService(AllMCTSReports mctsReportRepository) {
        this.mctsReportRepository = mctsReportRepository;
    }

    public List<MCTSReport> fetch(String date) throws Exception {
        try {
            return mctsReportRepository.allReportsToBeSentAsOf(date);
        } catch (Exception e) {
            logger.error(format("Fetching MCTS Reports for Date: {0} failed with exception : {1}",
                    date, getFullStackTrace(e)));
            throw e;
        }
    }

    public void markReportAsSent(MCTSReport report) {
        mctsReportRepository.markReportAsSent(report);
    }
}

