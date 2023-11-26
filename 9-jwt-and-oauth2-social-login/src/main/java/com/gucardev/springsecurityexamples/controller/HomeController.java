package com.gucardev.springsecurityexamples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @RequestMapping("/")
  public ModelAndView indexHtml() {
    return new ModelAndView("index");
  }

  @RequestMapping("/oauth2/redirect-success")
  public ModelAndView callBack(@RequestParam(name = "token", required = false) String token) {
    ModelAndView mav = new ModelAndView("callBack");
    mav.addObject("token", token);
    return mav;
  }
}
