package vakali;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

     Skill findByAccount(Account account);
     
     Skill findByAbility (String ability);
     
     
    
    
}
