package vakali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VoteRepository voteRepository;

    //Postaukset
    @GetMapping("/post")
    public String view(Model model) {

        Pageable pageable = PageRequest.of(0, 25, Sort.by("postDate").descending());

        model.addAttribute("posts", postRepository.findAll(pageable));

        return "post";
    }

    //Postauksen lisäys
    @PostMapping("/post")
    public String add(@RequestParam String post) {

        Post p = new Post();
        p.setContent(post);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        p.setUser(accountRepository.findByUsername(username));

        postRepository.save(p);

        return "redirect:/post";
    }

    //Postauksen tykkääminen
    @Transactional
    @PostMapping("/post/{postId}")
    public String like(Model model, @PathVariable(value = "postId") Long postId) {

        Account user = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Vote like = new Vote();

        if (voteRepository.findByUserAndPost(user, postRepository.getOne(postId)) == null) {

            like.setUser(user);
            like.setPost(postRepository.getOne(postId));
            voteRepository.save(like);
            postRepository.getOne(postId).setLikeCounter(postRepository.getOne(postId).getLikeCounter() + 1);
        }

        return "redirect:/post";

    }
}
