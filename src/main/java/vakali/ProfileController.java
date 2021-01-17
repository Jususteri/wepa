package vakali;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    @Autowired
    AccountRepository accountRepository;

    //Oma profiili
    @GetMapping("/profile")
    public String profile(Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("user", accountRepository.findByUsername(username));

        return "profile";
    }

    //Profiilikuvan lisäys
    @Transactional
    @PostMapping("/profilepicture")
    public String addProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!file.isEmpty()) {
            accountRepository.findByUsername(username).setContent(file.getBytes());

        } else if (file.isEmpty()) {
            accountRepository.findByUsername(username).setContent(null);
        }

        return "redirect:/profile";

    }

    //Profiilikuvan poisto
    @Transactional
    @PostMapping("/delete")
    public String deleteProfilePicture() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        accountRepository.findByUsername(username).setContent(null);

        return "redirect:/profile";

    }

    //Kaikki käyttäjät
    @GetMapping("/profiles")
    public String getAll(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        return "profiles";
    }

    //Profiilikuva
    @GetMapping(path = "/profilepicture/{name}", produces = "image/*")
    @ResponseBody
    public byte[] getOneProfilePicture(@PathVariable String name) {

        return accountRepository.findByNickname(name).getContent();
    }

    //Käyttäjä merkkijonolla
    @GetMapping("/{nickname}")
    public String getOneUser(Model model, @PathVariable String nickname) {

        if (accountRepository.findByNickname(nickname).getUsername()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {

            return "redirect:/profile";

        }

        model.addAttribute("user", accountRepository.findByNickname(nickname));
        return "single";
    }

    //Käyttäjän haku käyttäjänimellä
    @RequestMapping("/profiles/search")
    public String search(Model model, @RequestParam String username) {

        if (accountRepository.findByUsername(username) == null) {
            model.addAttribute("search", false);
            return "errorpage";
        }

        if (username.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return "redirect:/profile";
        }

        model.addAttribute("user", accountRepository.findByUsername(username));
        return "single";

    }
}
