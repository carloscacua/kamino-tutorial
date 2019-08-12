package com.mahisoft.tutorial.service.config;

import com.mahisoft.kamino.commonbeans.auth.KnownResources;
import com.mahisoft.kamino.commonbeans.auth.ResourceServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
@ConditionalOnProperty(
        name = {"oauth2.auth.jwt.keyValue"}
)
public class TutorialResourceServerConfiguration extends ResourceServerConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(KnownResources.OPEN_RESOURCES).permitAll()
                .antMatchers("/**").authenticated();
    }

}