# Spring Security

## Spring Security Overview

- Application security boils down to two more or less independent problems: authentication (who are you?) and authorization (what are you allowed to do?). 

- Spring Security has an architecture that is designed to separate authentication from authorization, and has strategies and extension points for both.

- The dependency for spring security is: `'org.springframework.boot:spring-boot-starter-security'`

---

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
    builder.jdbcAuthentication().dataSource(dataSource).withUser("user")
      .password("mySecret").roles("USER");
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

## Method Security/Authorization

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

## Retrieving the user and the roles

```java
@RequestMapping("/foo")
public String foo(Principal principal) {
  Authentication authentication = (Authentication) principal;
  User = (User) authentication.getPrincipal();
  ... // do stuff with user
}
```
---

## Retrieving the user and the roles

```java
import org.springframework.security.core.userdetails.User

@RequestMapping("/foo")
public String foo(@AuthenticationPrincipal User user) {
  ... // do stuff with user
}
```
---

## Retrieving the user and the roles

```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication authentication = context.getAuthentication();
authentication.isAuthenticated()
authentication.getName()
```

---
## Retrieving the user and the roles

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
---

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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/metric/**").hasRole("METRIC_READER")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().formLogin().permitAll()
                .and().logout().permitAll();
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/resources/**", "/css/**", "/js/**", "/images/**");
    }
}
```

---

## Password Encryption



Spring Security 5.x changed the way it handled encoded passwords. In previous versions, each application employed one password encoding algorithm only. 

Switching from one password encoder would be impossible without  affecting the users.

In spring security 5, the concept of password delegation is used. Spring recognizes the algorithm by an identifier prefixing the encoded password. e.g. **{md5}d55b7735123b290ca9f4084b903b8b5f**

If the password hash has no prefix, the delegation process uses a default encoder.

The default behavior is specified in `PasswordEncoderFactories.createDelegatingPasswordEncoder()`.

---

## Password Encryption

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

---

## CORS headers

- Which Requests are Cross-Origin Resource sharing requests

  Frontend script is served from different domain/sub-domain/port/schema from the domain that it tries to access.

- What browsers will do when a CORS is detected and what is the server side's desired behavior.

  Include `ORIGIN` request header which has the domain from which the script has been served and possibly `Access-Control-Request-Method` header indicating (in preflight/OPTIONS) requests the method and A`ccess-Control-Request-Headers`. 

  - At any case, the server needs to respond with whether accepts this CORS request or not by including the header

    **Access-Control-Allow-Origin**

    

---

This bean is the most flexible way of configuring CORS in spring boot:

```java
@Bean
public FilterRegistrationBean filterRegistrationBean() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
}
```

---

Another way is by configuring CORS in security level

```java
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    ...
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.
            cors().configurationSource(corsConfigurationSource())
```



Another final way of configuring CORS is by annotation at REST method or controller. Annotation is @CrossOrigin but this is not the proposed way.

---

## Adding Users and Roles

- The best way of adding users and roles is by using Flyway or Liquibase
- Another approach would be by using CommandLineRunner.
- Finally one can use SQL scripts (data.sql, schema.sql) which spring boot will run if these are located in /src/main/resources folder.

## Exercise

Modify Sensor service in such a way that it has spring security with username and password set by application.yml. The metrics can only be retrieved from another service only if these are known. Modify aggregator service code so that it can contact the secured sensor service.