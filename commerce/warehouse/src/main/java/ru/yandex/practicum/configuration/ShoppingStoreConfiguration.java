package ru.yandex.practicum.configuration;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingStoreConfiguration {

    //регистрация кастомного декодера для обработки ошибок
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().errorDecoder(new CustomErrorDecoder());
    }

}
