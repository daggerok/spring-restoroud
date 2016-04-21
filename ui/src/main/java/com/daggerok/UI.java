package com.daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Client
@EnableZuulProxy
@SpringCloudApplication
@EnableAutoConfiguration(exclude = {
        HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class
})
public class UI {
/*
    @Autowired
    private ResourceServerProperties resourceServerProperties;

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @RequestMapping("/")
    public Map<String, Object> index(Principal principal) throws IOException {
        return new LinkedHashMap<String, Object>() {{
            put("principal", principal);
            put("userInfoUri", resourceServerProperties.getUserInfoUri());
            put("resourceServerProperties", resourceServerProperties);
            put("token", oauth2RestTemplate.getOAuth2ClientContext().getAccessToken());
        }};
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(UI.class, args);
    }
}
