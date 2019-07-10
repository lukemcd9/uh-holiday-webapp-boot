package edu.hawaii.its.holiday.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(value = { "", "/" })
    public String home(Locale locale) {
        logger.info("User at home. The client locale is {}.", locale);
        return "home";
    }

    @GetMapping(value = "/contact")
    public String contact() {
        logger.info("User at contact.");
        return "contact";
    }

    @GetMapping(value = "/faq")
    public String faq() {
        logger.info("User at faq.");
        return "faq";
    }

    @GetMapping(value = "/admin")
    public String admin() {
        logger.info("User at admin.");
        return "admin";
    }

    @GetMapping(value = "/404")
    public String invalid() {
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/login")
    public String login() {
        logger.debug("User at login.");
        return "redirect:/";
    }
}
