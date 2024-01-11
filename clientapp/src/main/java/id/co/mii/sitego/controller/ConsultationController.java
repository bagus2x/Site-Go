package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Consultation;
import id.co.mii.sitego.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;

    @GetMapping("/consultation/manage")
    public String employeesView() {
        return "consultation/manage";
    }

    @GetMapping("/consultation/request")
    public String historyView(Model model) {
        List<Consultation> consultations = consultationService.getAllByProfile();
        boolean hasOngoingConsultation = consultationService.hasOngoingConsultation();
        model.addAttribute("hasOngoingConsultation", hasOngoingConsultation);
        model.addAttribute("consultations", consultations);

        return "consultation/request";
    }

    @PostMapping("/consultation/request")
    public String request() {
        consultationService.request();
        return "redirect:/consultation/request?success=true";
    }

    @GetMapping("/consultation/schedule")
    public String scheduleView() {
        return "consultation/schedule";
    }
}
