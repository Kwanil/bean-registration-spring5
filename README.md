# bean-registration-spring5
- spring5에서는 bean 등록방식이 몇가지 변경되었다.
- 3가지 방식으로 sample 코드를 추가해본다.
- spring boot 2.0 로 하였으며, infoq 동영상을 참조/정리하였다.
- https://www.infoq.com/presentations/bean-registration-spring-5


#legacy
- 기존의 bean 주입방식
- annotation의 사용 방식

```
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

```

#spring5 -java
spring5에서 추가된 방식
- `SpringApplicationBuilder`를 통해 선별적으로 주입이 가능하다.
- lambda의 무분별한 사용으로 인해 가독성이 떨어지는 단점이 있다.

```
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

```
#spring5 -kotlin
- dsl을 사용한 방식으로 가독성은 좋다
- 지원하는 dsl이 무엇인지 확인을 해야하는 단점. 그리고 낯선 언어.
```
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
```

이럴바엔....
Spring4부터 지원하는 groovy dsl방식이 어떤지.. 