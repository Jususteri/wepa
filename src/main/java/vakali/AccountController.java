package vakali;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //Rekisteröinti
    @GetMapping("/registration")
    public String register(Model model) {
        return "registration";
    }

    //Rekisteröinti
    @PostMapping("/registration")
    public String registration(Model model, @RequestParam String username, @RequestParam String password,
            @RequestParam("file") MultipartFile file, @RequestParam String name, @RequestParam String nickname) throws IOException {

        if (accountRepository.findByUsername(username) != null) {
            model.addAttribute("username", false);
            return "errorpage";
        }

        if (accountRepository.findByNickname(nickname) != null) {
            model.addAttribute("nickname", false);
            return "errorpage";
        }

        Account a = new Account();

        if (!file.isEmpty()) {
            a.setContent(file.getBytes());
        } else if (file.isEmpty()) {
            a.setContent(null);
        }

        a.setUsername(username);
        a.setPassword(passwordEncoder.encode(password));
        a.setName(name);
        a.setNickname(nickname);
        accountRepository.save(a);

        return "redirect:/login";
    }

    //Kirjautuminen
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
}
