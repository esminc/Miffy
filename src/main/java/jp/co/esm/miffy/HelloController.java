package jp.co.esm.miffy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

  @RequestMapping("/")
  public String page(Model model) {
    model.addAttribute("message","Hello Spring Boot!");
    return "index";
  }
}
