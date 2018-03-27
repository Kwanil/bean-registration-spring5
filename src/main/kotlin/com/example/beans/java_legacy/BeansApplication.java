package com.example.beans.java_legacy;

import com.mongodb.MongoClient;
import lombok.ToString;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@SpringBootApplication
public class BeansApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeansApplication.class,args);
    }

    @Bean
    RouterFunction<?> routes() {
        return route(GET("/hi"),request -> ok().body(just("Hello world!"),String.class));
    }

    @Bean
    ApplicationRunner runner(CustomerService customerService) {
        return args -> System.out.println(customerService.toString());
    }

    @Bean
    CustomerService customerService(MongoClient mongoClient) {
        return new CustomerService(mongoClient);
    }

}

@ToString
class CustomerService {
    private final MongoClient mongoClient;

    CustomerService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
}
