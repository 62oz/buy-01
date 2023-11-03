package buy01.authservice.config;

@Configuration
public class RestClientConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

