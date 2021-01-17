package vakali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    //Kommentointi
    @GetMapping("/post/comment/{postId}")
    public String comment(Model model, @PathVariable(value = "postId") Long postId) {

        model.addAttribute("post", postRepository.getOne(postId));

        return "comment";

    }

    //Kommentin lisäys
    @PostMapping("/post/comment/{postId}")
    public String addComment(Model model, @PathVariable(value = "postId") Long postId, @RequestParam String comment) {

        Comment c = new Comment();

        c.setPost(postRepository.getOne(postId));
        c.setContent(comment);
        c.setUser(accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        commentRepository.save(c);

        model.addAttribute("post", postRepository.getOne(postId));

        return "comment";
    }

}
