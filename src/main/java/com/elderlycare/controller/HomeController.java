package com.elderlycare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  // 「/」 和 「/home」 都映射到 templates/home.html
  @GetMapping({"/", "/home"})
  public String home() {
    return "home";
  }
}
