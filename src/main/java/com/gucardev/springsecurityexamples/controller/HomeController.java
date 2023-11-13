package com.gucardev.springsecurityexamples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @RequestMapping("/")
  public ModelAndView indexHtml() {
    return new ModelAndView("index");
  }
  @RequestMapping("/oauth2/redirectCustom")
  public ModelAndView callBack() {
    return new ModelAndView("callBack");
  }
}
