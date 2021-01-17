package vakali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SkillController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SkillRepository skillRepository;

    //Taidon lisäys
    @PostMapping("/skill")
    public String addSkill(@RequestParam String skill) {

        Skill s = new Skill();
        s.setAbility(skill.trim());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        s.setAccount(accountRepository.findByUsername(username));
        s.setPraiseCounter(0);
        skillRepository.save(s);

        return "redirect:/profile";
    }

    //Taidon kehuminen  
    @Transactional
    @PostMapping("/skill/{skillId}")
    public String like(Model model, @PathVariable(value = "skillId") Long skillId) {

        String username = skillRepository.getOne(skillId).getAccount().getUsername();

        skillRepository.getOne(skillId).setPraiseCounter(skillRepository.getOne(skillId).getPraiseCounter() + 1);

        model.addAttribute("user", accountRepository.findByUsername(username));

        return "single";
    }

}
