package id.co.mii.sitego.service;

import id.co.mii.sitego.model.Consultation;
import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.response.DashboardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class DashboardService {
    private final ProfileService profileService;
    private final ConsultationService consultationService;

    public DashboardResponse getDashboardStat() {
        List<Consultation> consultations = consultationService.getAll();
        List<Profile> profiles = profileService.getAll(new HashSet<String>() {{
            add("ADMIN");
            add("EMPLOYEE");
            add("CONSULTANT");
        }});

        return DashboardResponse.builder()
            .userStat(getUserStat(profiles))
            .consultationBars(generateConsultationBars(consultations))
            .consultationGrowth(generateConsultationGrowth(consultations))
            .consultationCounter(getConsultationCounter(consultations))
            .build();
    }

    public DashboardResponse.UserStat getUserStat(List<Profile> profiles) {
        long numberOfEmployees = profiles.stream()
            .filter(profile -> profile.getUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("EMPLOYEE"))
            )
            .count();

        long numberOfConsultants = profiles.stream()
            .filter(profile -> profile.getUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("CONSULTANT"))
            )
            .count();

        long numberOfAdmins = profiles.stream()
            .filter(profile -> profile.getUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ADMIN"))
            )
            .count();

        return DashboardResponse.UserStat.builder()
            .numberOfUsers(profiles.size())
            .numberOfEmployees((int) numberOfEmployees)
            .numberOfConsultants((int) numberOfConsultants)
            .numberOfAdmins((int) numberOfAdmins)
            .build();
    }

    public DashboardResponse.ConsultationCounter getConsultationCounter(List<Consultation> consultations) {
        long numberOfRejected = consultations.stream()
            .filter(consultation -> consultation
                .getStatus()
                .getName()
                .equals("REJECTED"))
            .count();

        long numberOfWaiting = consultations.stream()
            .filter(consultation -> consultation
                .getStatus()
                .getName()
                .equals("WAITING"))
            .count();

        long numberOfValidated = consultations.stream()
            .filter(consultation -> consultation
                .getStatus()
                .getName()
                .equals("VALIDATED"))
            .count();

        long numberOfScheduled = consultations.stream()
            .filter(consultation -> consultation
                .getStatus()
                .getName()
                .equals("SCHEDULED"))
            .count();

        long numberOfDone = consultations.stream()
            .filter(consultation -> consultation
                .getStatus()
                .getName()
                .equals("DONE"))
            .count();

        return DashboardResponse.ConsultationCounter.builder()
            .numberOfConsultations(consultations.size())
            .numberOfRejected((int) numberOfRejected)
            .numberOfWaiting((int) numberOfWaiting)
            .numberOfValidated((int) numberOfValidated)
            .numberOfScheduled((int) numberOfScheduled)
            .numberOfDone((int) numberOfDone)
            .build();

    }

    public DashboardResponse.ConsultationGrowth generateConsultationGrowth(List<Consultation> consultations) {
        LocalDateTime now = LocalDateTime.now();

        long numberOfConsultationsOnThisYear = consultations.stream()
            .filter(consultation -> consultation.getCreatedAt().getYear() == now.getYear())
            .count();

        long numberOfConsultationsOnLastYear = consultations.stream()
            .filter(consultation -> consultation.getCreatedAt().getYear() == now.getYear() - 1)
            .count();

        return DashboardResponse.ConsultationGrowth.builder()
            .numberOfConsultations(consultations.size())
            .numberOfConsultationsOnLastYear((int) numberOfConsultationsOnLastYear)
            .numberOfConsultationsOnThisYear((int) numberOfConsultationsOnThisYear)
            .build();
    }

    public List<DashboardResponse.ConsultationBar> generateConsultationBars(List<Consultation> consultations) {
        Map<String, List<Integer>> statsMap = new HashMap<>();

        for (Consultation consultation : consultations) {
            LocalDateTime createdAt = consultation.getCreatedAt();
            String year = String.valueOf(createdAt.getYear());

            statsMap.putIfAbsent(year, new ArrayList<>());

            int monthIndex = createdAt.getMonthValue() - 1;
            while (statsMap.get(year).size() < 12) {
                statsMap.get(year).add(0);
            }
            statsMap.get(year).set(monthIndex, statsMap.get(year).get(monthIndex) + 1);
        }

        List<DashboardResponse.ConsultationBar> consultationBars = new ArrayList<>();
        statsMap.forEach((year, data) -> consultationBars.add(new DashboardResponse.ConsultationBar(year, data)));

        return consultationBars;
    }
}
