package cz.cvut.zuul.oaas.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String getIndex() {
        return "index";
    }

    @RequestMapping("/login")
    public String getLogin() {
        return "login";
    }
}
