package id.co.mii.sitego.service;

import id.co.mii.sitego.model.Consultation;
import id.co.mii.sitego.model.request.CheckConflictScheduleRequest;
import id.co.mii.sitego.model.request.FinishConsultationRequest;
import id.co.mii.sitego.model.request.RejectConsultationRequest;
import id.co.mii.sitego.model.request.ScheduleConsultationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final RestTemplate restTemplate;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public Consultation request() {
        return restTemplate.postForObject(baseUrl + "/consultation", null, Consultation.class);
    }

    public List<Consultation> getAllByProfile() {
        Consultation[] consultations = restTemplate.getForEntity(baseUrl + "/profile/consultations", Consultation[].class).getBody();
        return Arrays.asList(Objects.requireNonNull(consultations));
    }

    public boolean hasOngoingConsultation() {
        List<Consultation> consultations = getAllByProfile();
        if (consultations.isEmpty()) {
            return false;
        }

        Consultation latestConsultation = consultations.get(0);
        String status = latestConsultation.getStatus().getName();

        return status.equals("WAITING") || status.equals("VALIDATED") || status.equals("SCHEDULED");
    }

    public List<Consultation> getAll() {
        Consultation[] consultations = restTemplate.getForEntity(baseUrl + "/consultations", Consultation[].class).getBody();
        return Arrays.asList(Objects.requireNonNull(consultations));
    }

    public Consultation getById(Integer consultationId) {
        return restTemplate.getForObject(baseUrl + "/consultation/" + consultationId, Consultation.class);
    }

    public Consultation validate(Integer consultationId) {
        return restTemplate.patchForObject(baseUrl + "/consultation/" + consultationId + "/validate", null, Consultation.class);
    }

    public Consultation reject(Integer consultationId, RejectConsultationRequest request) {
        Consultation consultation = restTemplate.patchForObject(baseUrl + "/consultation/" + consultationId + "/reject", request, Consultation.class);

        sendRejectionEmail(Objects.requireNonNull(consultation));

        return consultation;
    }

    public Consultation schedule(@PathVariable Integer consultationId, ScheduleConsultationRequest request) {
        Consultation consultation = restTemplate.patchForObject(baseUrl + "/consultation/" + consultationId + "/schedule", request, Consultation.class);

        sendInvitationEmail(Objects.requireNonNull(consultation));

        return consultation;
    }

    private void sendInvitationEmail(Consultation consultation) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(consultation.getCounselee().getUser().getEmail());
            helper.setCc(consultation.getConsultant().getUser().getEmail());

            String subject = String.format("Hi %s! You have been invited to join consultation with %s!", consultation.getCounselee().getName(), consultation.getConsultant().getName());
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("consultation", consultation);

            String html = templateEngine.process("components/consultation/email-consultation", context);

            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRejectionEmail(Consultation consultation) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(consultation.getCounselee().getUser().getEmail());

            String subject = String.format("Hi %s! Your request has been rejected!", consultation.getCounselee().getName());
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("consultation", consultation);

            String html = templateEngine.process("components/consultation/email-rejection", context);

            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Consultation finish(@PathVariable Integer consultationId, FinishConsultationRequest request) {
        return restTemplate.patchForObject(baseUrl + "/consultation/" + consultationId + "/finish", request, Consultation.class);
    }

    public boolean checkConflictSchedule(CheckConflictScheduleRequest request) {
        return Boolean.TRUE.equals(restTemplate.postForObject(baseUrl + "/consultation/check-conflict-schedule", request, Boolean.class));
    }
}
