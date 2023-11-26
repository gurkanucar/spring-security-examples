# spring-security-examples

## 1) In Memory Auth

### With this example, you will understand the spring security mechanism. No need to database. Just we are adding some users and authorities (roles). In this example we used Basic Authentication (username, password)
```java
@Bean
  public UserDetailsService users() {
    UserDetails user =
        User.builder()
            .username("user")
            .password("pass")
            .passwordEncoder(passwordEncoder::encode)
            // .password(passwordEncoder.encode("pass"))
            .roles("USER")
            .build();
        ...
        return new InMemoryUserDetailsManager(user, admin, mod);
}
```

- to allow endpoints we can use:
```java

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring()
            .requestMatchers(
                new AntPathRequestMatcher("/auth/**"),
                new AntPathRequestMatcher("/public/**"));
  }
```
- also we can configure http security options:

```java
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        // authenticate any request except web.ignoring()
        // also you can allow some endpoints here:
        // x.requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
        .authorizeHttpRequests(x -> x.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return httpSecurity.build();
  }
```

## 2 - 3 ) Basic Authentication with Hardcoded Enum Roles

### With this example, you will able to add hardcoded roles to user. Many projects we don't need to add dynamic roles and store them via different table in database.

#### NOTE: After first example, if we want to retrieve users from database we have to implement our custom user details service, and use spring-security's UserDetails class instead our User class:

```java

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userService.getByUsername(username);
    if (user.isEmpty()) {
      throw new EntityNotFoundException();
    }
    return new CustomUserDetails(user.get());
  }
}



public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}



```


## 4 ) Basic Authentication with Dynamic Roles

### With this example, you will able to add dynamic roles to user and store these roles in database.

```java
.....
public class User extends BaseEntity {

    .....

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;

}

....
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "role_name")
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return getName();
    }
}



```



## 5 ) Give Access or Block to endpoints Dynamically By Role

### With this example, you will able to allow or block access to endpoints by Roles. We are using AuthorizationManager (AccessDecisionVoter is deprecated) to decide.

```java


@Component
@RequiredArgsConstructor
public class RoleBasedVoter implements AuthorizationManager<RequestAuthorizationContext> {

  private final RoleRepository roleRepository;

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext object) {
    if (authentication.get().getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.get().getPrincipal();
      String requestUrl = object.getRequest().getRequestURI();

      List<Role> roles = roleRepository.findByUsers_Username(userDetails.getUsername());

      for (Role role : roles) {
        if (role.getRestrictedEndpoints().contains(requestUrl)) {
          return new AuthorizationDecision(false);
        }
      }
    }

    return new AuthorizationDecision(true);
  }
}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final RoleBasedVoter roleBasedVoter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                ....
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(x -> x.anyRequest().access(roleBasedVoter))
                ....

```


## 6 7 8 9 ) JWT examples

- JWT is a token based authentication mechanism. Once you got token, you can use it until expire. Also we use refresh token to get new token after your access token get expired


-------------------------------------------------------------------

### Demo video:
[https://www.youtube.com/watch?v=BoioooM1vL8](https://www.youtube.com/watch?v=BoioooM1vL8)

-------------------------------------------------------------------


### Special thanks to `Ivan Franchin` for oauth2 example below:
[https://github.com/ivangfr/springboot-react-social-login](https://github.com/ivangfr/springboot-react-social-login)