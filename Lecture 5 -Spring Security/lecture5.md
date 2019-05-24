# Spring Security

## Spring Security Overview

- Application security boils down to two more or less independent problems: authentication (who are you?) and authorization (what are you allowed to do?). 

- Spring Security has an architecture that is designed to separate authentication from authorization, and has strategies and extension points for both.

- The dependency for spring security is: `'org.springframework.boot:spring-boot-starter-security'`

  ## Authentication

- The main interface for authentication is `AuthenticationManager` which only has one method `Authentication authenticate(Authentication authentication)`

- 
  The most commonly used implementation of `AuthenticationManager` is `ProviderManager`.

- Spring Security provides some configuration helpers to quickly get common authentication manager features set up in your application. Most commonly this is done using `WebSecurityConfigurerAdapter`


```java
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
  @Autowired
  DataSource dataSource;

  @Override
  public configure(AuthenticationManagerBuilder builder) {
    builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
      .password("secret").roles("USER");
  }
}
```

## Security filter/Authorization

```java
@Configuration
public class ApplicationConfigurerAdapter extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
      .authorizeRequests()
        .antMatchers("/sensor/**").hasRole("SENSOR")
        .antMatchers("/metric/**").hasRole("METRIC")
        .anyRequest().isAuthenticated();
     ...;
  }
}
```

## Method Security

-   @EnableGlobalMethodSecurity(prePostEnabled= true), this enables security based on annotation `@PreAuthorize`
-   `@PreAuthorize` can be in class or method level and supports Spell.

```java
@PreAuthorize("hasAnyAuthority('VIEW','READ', 'WRITE')")
@PreAuthorize("hasAnyRole('VIEWER','READER', 'WRITER')")
@PreAuthorize("hasAuthority('VIEW')")
@PreAuthorize("hasRole('VIEWER'")

```
- Check the following class for more:

```java
org.springframework.security.access.expression.SecurityExpressionRoot
```

- Notice the difference between `hasRole` and `hasAuthority`. for roles VIEWER is same as if in database exists a role named **ROLE_**VIEWER

---

## Receiving the user and the roles

```java
@RequestMapping("/foo")
public String foo(Principal principal) {
  Authentication authentication = (Authentication) principal;
  User = (User) authentication.getPrincipal();
  ... // do stuff with user
}
```

```java
import org.springframework.security.core.userdetails.User

@RequestMapping("/foo")
public String foo(@AuthenticationPrincipal User user) {
  ... // do stuff with user
}
```

```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication authentication = context.getAuthentication();
authentication.isAuthenticated()
authentication.getName()
```

```java
public void foo(Authentication authentication) {
    List<String> userAuthorities = new ArrayList<>();
    if (authentication != null) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            userAuthorities.add(authority.toString());
        }
    }    
}
```

## Example

- Set how to connect JPA database User with Spring security
- Enable web security and Configure Authentication manager to use the provided authentication provider.
- Configure HttpSecurity filters
- Configure WebSecurity filters
- Enable spring security annotations
- Create the user in the database

---

```java
@Configuration
@RequiredArgsConstructor
public class UserDetailsConfiguration {

    @SuppressWarnings("Convert2Lambda")
    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new UserDetailsService() {
            @Override
            @Transactional
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return accountRepository.findById(username).map(
                        user -> new org.springframework.security.core.userdetails.User(user.getEmail(),
                                user.getPassword(),
                                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                ).orElseThrow(() -> new UsernameNotFoundException("Email " + username + "was not found"));
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }
}
```

---

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DaoAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/metric/**").hasRole("METRIC_READER")
                .anyRequest()
                .authenticated()
                .and().httpBasic()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
```



<https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/>

remebmer to explain liquidbase in web

<https://www.baeldung.com/spring-security-5-oauth2-login>

Remmber the encription