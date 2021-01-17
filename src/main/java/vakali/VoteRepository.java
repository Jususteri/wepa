package vakali;


import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

   Vote findByUserAndPost(Account user, Post post);
   
}
