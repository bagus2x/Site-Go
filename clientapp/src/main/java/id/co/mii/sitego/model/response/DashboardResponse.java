package id.co.mii.sitego.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DashboardResponse {
    private UserStat userStat;

    private List<ConsultationBar> consultationBars;

    private ConsultationGrowth consultationGrowth;

    private ConsultationCounter consultationCounter;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class UserStat {
        private Integer numberOfEmployees;

        private Integer numberOfConsultants;

        private Integer numberOfAdmins;

        private Integer numberOfUsers;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ConsultationBar {
        private String year;

        private List<Integer> data;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ConsultationGrowth {
        private Integer numberOfConsultations;

        private Integer numberOfConsultationsOnLastYear;

        private Integer numberOfConsultationsOnThisYear;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ConsultationCounter {
        private Integer numberOfRejected;

        private Integer numberOfWaiting;

        private Integer numberOfValidated;

        private Integer numberOfScheduled;

        private Integer numberOfDone;

        private Integer numberOfConsultations;
    }
}
