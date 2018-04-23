package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.UserRepository;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){

        model.addAttribute("view","article/create");
        return "base-layout";

    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity =this.userRepository.findByUserName(user.getUsername());
        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity
        );

        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";

    }
    @GetMapping("/{id}")
    public String details(Model model, @PathVariable Integer id){

        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);
        model.addAttribute("article",article);
        model.addAttribute("view","article/details");

        return "base-layout";

    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id,Model model){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        model.addAttribute("view","article/edit");
        model.addAttribute("article",article);

        return "base-layout";
    }


    @PostMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id,ArticleBindingModel articleBindingModel){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/"+article.getId();
    }

    @GetMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);
        model.addAttribute("view","article/delete");
        model.addAttribute("article",article);
        return "base-layout";

    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(Model model, @PathVariable Integer id){

        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        this.articleRepository.delete(article);


        return "redirect:/";


    }


}