package de.propra.exambyte.controller.corrector;

import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorrectorController {

  private TestService testService;

  @GetMapping("/corrector")
  public String index() {
    return "corrector";
  }
}
