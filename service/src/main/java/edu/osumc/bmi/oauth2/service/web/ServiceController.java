package edu.osumc.bmi.oauth2.service.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

  @GetMapping("/api/hello")
  @ResponseBody
  public String hello() {
    return "hello world!";
  }
}