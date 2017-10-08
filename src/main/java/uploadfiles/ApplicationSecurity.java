package uploadfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by piotrek on 01.09.17.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final String USER_QUERY="select username,password,enabled from users where username=?";
    private static final String AUTHORITY_QUERY="select u.username, r.name " +
            "from users u, roles r, user_roles ur " +
            "where u.username=? and u.id = ur.user_id and r.id = ur.role_id";

    @Autowired
    private DataSource source;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests().antMatchers("/uploadfiles/**").hasAuthority("ADMIN")//access("hasRole('ADMIN')");
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/", "/registration").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").failureUrl("/login?error").permitAll().and()
                .logout().permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
/*        auth.inMemoryAuthentication().withUser("admin").password("admin")
                .roles("ADMIN", "USER").and().withUser("user").password("user")
                .roles("USER")*/;
        auth.jdbcAuthentication().dataSource(source).
                usersByUsernameQuery(USER_QUERY).
                authoritiesByUsernameQuery(AUTHORITY_QUERY);
    }

}