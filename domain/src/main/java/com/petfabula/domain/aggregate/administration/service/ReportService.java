package com.petfabula.domain.aggregate.administration.service;

import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.entity.ReportReason;
import com.petfabula.domain.aggregate.administration.repository.ReportReasonRepository;
import com.petfabula.domain.aggregate.administration.repository.ReportRepository;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportReasonRepository reportReasonRepository;

    @Autowired
    private AdminIdGenerator idGenerator;

    public Report create(Long reporterId, String reason, String entityType, Long entityId) {
        UserAccount account = userAccountRepository.findById(reporterId);
        if (account == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (Report.ReportType.POST.toString().equals(entityType)) {
            Post post = postRepository.findById(entityId);
            if (post == null || post.getParticipator().getId().equals(reporterId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        } else if (Report.ReportType.QUESTION.toString().equals(entityType)) {
            Question question = questionRepository.findById(entityId);
            if (question == null || question.getParticipator().getId().equals(reporterId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        } else if (Report.ReportType.ANSWER.toString().equals(entityType)) {
            Answer answer = answerRepository.findById(entityId);
            if (answer == null || answer.getParticipator().getId().equals(reporterId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        }

        Report report = reportRepository.findByEntityIdAndEntityType(entityId, entityType);
        if (report == null) {
            Long reportId = idGenerator.nextId();
            report = new Report(reportId, entityType, entityId, reporterId, reason);

            Long reasonId = idGenerator.nextId();
            ReportReason reportReason = new ReportReason(reasonId, reportId, reporterId, reason);

            report = reportRepository.save(report);
            reportReasonRepository.save(reportReason);

            return report;
        } else {
            ReportReason reportReason = reportReasonRepository
                    .findByReportIdAndReporterId(report.getId(), reporterId);
            if (reportReason != null) {
                reportReason.setReason(reason);
                reportReasonRepository.save(reportReason);
                return report;
            }

            report.setReportCount(report.getReportCount() + 1);
            report.setRecentReporterId(reporterId);
            report.setRecentReason(reason);

            Long reasonId = idGenerator.nextId();
            reportReason = new ReportReason(reasonId, report.getId(), reporterId, reason);
            reportReasonRepository.save(reportReason);

            report = reportRepository.save(report);
            return report;
        }
    }

    public Report updateState(Long reportId, Report.ReportStatus status) {
        Report report = reportRepository.findById(reportId);
        if (report == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        report.setStatus(status);
        return reportRepository.save(report);
    }

    public Report updateMemo(Long reportId, String memo) {
        Report report = reportRepository.findById(reportId);
        if (report == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        report.setMemo(memo);
        return reportRepository.save(report);
    }

}
