package org.ei.drishti.form.service;

import ch.lambdaj.function.convert.Converter;
import org.ei.drishti.common.util.DateUtil;
import org.ei.drishti.dto.form.FormSubmissionDTO;
import org.ei.drishti.form.domain.FormSubmission;
import org.ei.drishti.form.repository.AllFormSubmissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static java.util.Collections.sort;

@Service
public class FormSubmissionService {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionService.class.toString());
    private AllFormSubmissions allFormSubmissions;

    @Autowired
    public FormSubmissionService(AllFormSubmissions allFormSubmissions) {
        this.allFormSubmissions = allFormSubmissions;
    }

    public List<FormSubmissionDTO> fetch(long formFetchToken) {
        return with(allFormSubmissions.findByServerVersion(formFetchToken)).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }

    public List<FormSubmission> getNewSubmissionsForANM(String anmIdentifier, Long version, Integer batchSize) {
        return allFormSubmissions.findByANMIDAndServerVersion(anmIdentifier, version, batchSize);
    }

    public List<FormSubmission> getAllSubmissions(Long version, Integer batchSize) {
        return allFormSubmissions.allFormSubmissions(version, batchSize);
    }

    public void submit(List<FormSubmissionDTO> formSubmissionsDTO) {
        List<FormSubmission> formSubmissions = with(formSubmissionsDTO).convert(new Converter<FormSubmissionDTO, FormSubmission>() {
            @Override
            public FormSubmission convert(FormSubmissionDTO submission) {
                return FormSubmissionConverter.toFormSubmission(submission);
            }
        });

        sort(formSubmissions, timeStampComparator());
        for (FormSubmission submission : formSubmissions) {
            if (allFormSubmissions.exists(submission.instanceId())) {
                logger.warn(format("Received form submission that already exists. Skipping. Submission: {0}", submission));
                continue;
            }
            logger.info(format("Saving form {0} with instance Id: {1} and for entity Id: {2}",
                    submission.formName(), submission.instanceId(), submission.entityId()));
            submission.setServerVersion(DateUtil.millis());
            allFormSubmissions.add(submission);
        }
    }
    
    public List<FormSubmission> getSubmissionByFormName(String formName, long serverVersion) {
        return allFormSubmissions.findByFormName(formName, serverVersion);
    }
    
    public List<FormSubmission> getSubmissionByOpenmrsNotSynced(String formName) {
        return allFormSubmissions.findByOpenmrsNotSynced(formName);
    }
    
    public void update(FormSubmission fs) {
		allFormSubmissions.update(fs);
	}


    private Comparator<FormSubmission> timeStampComparator() {
        return new Comparator<FormSubmission>() {
            public int compare(FormSubmission firstSubmission, FormSubmission secondSubmission) {
                long firstTimestamp = firstSubmission.clientVersion();
                long secondTimestamp = secondSubmission.clientVersion();
                return firstTimestamp == secondTimestamp ? 0 : firstTimestamp < secondTimestamp ? -1 : 1;
            }
        };
    }
}
