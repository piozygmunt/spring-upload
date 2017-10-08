package uploadfiles.controllers;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by piotrek on 01.09.17.
 */
@Controller
public class MainController extends WebMvcConfigurerAdapter{
    private final Logger logger = Logger.getLogger(MainController.class);

    @GetMapping("/")
    public String index(Principal principal, Authentication auth)
    {
       /* if(principal != null )
        {*/
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            authorities.forEach(authority -> logger.info(authority.toString()));

       /*     logger.info(principal.getName());
        }*/
        return "index";
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/").setViewName("index");
    }

}
