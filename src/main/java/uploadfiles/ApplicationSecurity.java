package uploadfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by piotrek on 01.09.17.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final String USER_QUERY="select username,password,enabled from users where username=?";
    private static final String AUTHORITY_QUERY="select username, role from user_roles where username=?";

    @Autowired
    private DataSource source;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests().antMatchers("/uploadfiles/**").hasAuthority("ADMIN")//access("hasRole('ADMIN')");
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/").permitAll()
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