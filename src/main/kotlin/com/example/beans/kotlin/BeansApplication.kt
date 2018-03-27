package com.example.beans

import com.mongodb.MongoClient
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono.just

@SpringBootApplication
class BeansApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(BeansApplication::class.java)
            .initializers(
                beans {
                    bean<CustomerService>()
                    bean {
                        ApplicationRunner {
                            println(ref<CustomerService>().toString())
                        }
                    }
                    bean {
                        router {
                            GET("/hi") {
                                ok().body(just("Hello world!!"))
                            }
                        }
                    }
                }
            )
            .run(*args)
}

class CustomerService(val mongoClient: MongoClient)
