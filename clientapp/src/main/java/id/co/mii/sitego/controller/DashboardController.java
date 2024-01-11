package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.response.DashboardResponse;
import id.co.mii.sitego.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Profile authProfile = (Profile) SecurityContextHolder.getContext().getAuthentication().getDetails();
        boolean hasAccess = authProfile.getUser().getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("CONSULTANT"));
        if (!hasAccess) {
            return "redirect:/profile";
        }

        DashboardResponse dashboardStat = dashboardService.getDashboardStat();
        model.addAttribute("dashboardStat", dashboardStat);

        return "dashboard/dashboard";
    }
}
