package vakali;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByNickname(String nickname);

    Account findByName(String name);
}
