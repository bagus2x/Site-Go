package id.co.mii.sitego.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrganizationController {

    @GetMapping("/organization/role")
    public String rolesView() {
        return "organization/role";
    }

    @GetMapping("/organization/employee")
    public String employeesView() {
        return "organization/employee";
    }
}
