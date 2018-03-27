package com.example.beans.java;

import com.mongodb.MongoClient;
import lombok.ToString;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@SpringBootApplication
public class BeansApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .initializers((ApplicationContextInitializer<GenericApplicationContext>) context -> {
                    context.registerBean(CustomerService.class);
                    context.registerBean(ApplicationRunner.class,
                            () -> args1 -> System.out.println(context.getBean(CustomerService.class).toString()));
                    context.registerBean(RouterFunction.class,
                            () -> route(GET("/hi"),request -> ok().body(just("Hello world!"),String.class)));
                })
                .sources(BeansApplication.class)
                .run(args);
    }
}

@ToString
class CustomerService {
    private final MongoClient mongoClient;

    CustomerService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
}

