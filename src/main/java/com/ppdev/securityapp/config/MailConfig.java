package com.ppdev.securityapp.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application.properties")

public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(env.getProperty("spring.mail.port", Integer.class));

        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));
        Properties props = new Properties();
        props.put("mail.transport.protocol", env.getProperty("spring.mail.properties.mail.transport.protocol"));
        props.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        props.put("mail.debug", env.getProperty("spring.mail.properties.mail.debug"));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
