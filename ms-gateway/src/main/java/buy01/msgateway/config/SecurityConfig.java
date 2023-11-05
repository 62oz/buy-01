package buy01.msgateway.config;

@Configuration
@EnableWebSecurity
public class GatewaySecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthServiceClient authServiceClient;

    public GatewaySecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthServiceClient authServiceClient) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authServiceClient = authServiceClient;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/login", "/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
