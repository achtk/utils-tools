package com.chua.utils.tools.example;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/15
 */
public class ReactorExample {

    public static void main(String[] args) throws Exception {
        //testReactor();
        HttpClient httpClient = HttpClient.create();
        HttpClient.ResponseReceiver<?> httpClientResponse = httpClient.secure(sslContextSpec -> {
            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE));
        }).get().uri("https://gitee.com/mirrors/reactor-netty");

        WebClient webClient = WebClient.create();
        ClientResponse clientResponse =
                webClient.get().uri("https://gitee.com/mirrors/reactor-netty").exchange().block();

        String string = clientResponse.bodyToMono(String.class).block();
        System.out.println(string);
//        String block = httpClientResponse.responseContent()
//                .aggregate()
//                .asString().block();
        //System.out.println(block);


    }

    private static void testReactor() throws Exception {
        System.out.println("=================================== Flux =======================================");

        // 整型
        Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5);
        // 字符串
        Flux<String> stringFlux = Flux.just("hello", "world");
        // 从list创建
        List<String> list = Arrays.asList("hello", "world");
        Flux<String> stringFlux1 = Flux.fromIterable(list);
        // 范围
        Flux<Integer> integerFlux1 = Flux.range(1, 5);

        // 每1秒产生一个数据
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(1000));
        longFlux.take(10).subscribe(System.out::println);

        // 从Flux创建
        Flux<String> stringFlux2 = Flux.from(stringFlux1);
        stringFlux2.subscribe(System.out::println);


        System.out.println("=================================== Mono =======================================");

        // 字符串
        Mono<String> stringMono = Mono.just("Hello World");
        // Callable创建
        Mono<String> stringMono1 = Mono.fromCallable(() -> "Hello World");
        // Future创建
        Mono<String> stringMono2 = Mono.fromFuture(CompletableFuture.completedFuture("Hello World"));
        // Suppier创建
        Random random = new Random();
        Mono<Double> doubleMono = Mono.fromSupplier(random::nextDouble);
        //Mono创建
        Mono<Double> doubleMono1 = Mono.from(doubleMono);
        // Flux创建，mono只能拿到一个数据
        Mono<Integer> integerMono = Mono.from(Flux.range(1, 5));
        integerMono.subscribe(System.out::println);
        stringMono2.subscribe(System.out::println);


        System.out.println("=================================== subscribe订阅 =======================================");

        // 订阅方式一
        stringFlux = Flux.just("Hello", "World");
        stringFlux.subscribe(val -> {
            System.out.println("val:" + val);
        }, error -> {
            System.out.println("error:" + error);
        }, () -> {
            System.out.println("Finished");
        }, subscription -> {
            subscription.request(1);
        });
        // 订阅方式二
        stringFlux.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext:" + s);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });


        System.out.println("=================================== map映射 =======================================");

        class Employee {
            public int id;
            public String name;
            public double salary;

            Employee(int id, String name, double salary) {
                this.id = id;
                this.name = name;
                this.salary = salary;
            }

            public String getName() {
                return name;
            }

            public double getSalary() {
                return salary;
            }
        }

        class Leader {
            public String name;
            public double salary;

            Leader(String name, double salary) {
                this.name = name;
                this.salary = salary;
            }
        }

        List<Employee> EmployeeList = Arrays.asList(
                new Employee(1, "Alex", 1000),
                new Employee(2, "Michael", 2000),
                new Employee(3, "Jack", 1500),
                new Employee(4, "Owen", 1500),
                new Employee(5, "Denny", 2000));

        Flux<Employee> employeeFlux = Flux.fromIterable(EmployeeList);
        employeeFlux.map(employee -> {
            Leader leader = new Leader(employee.name, employee.salary);
            return leader;
        }).subscribe(consumer -> System.out.println(consumer.name));

        // 加过滤器
        employeeFlux.filter(employee -> 2000 == employee.salary)
                .map(employee -> {
                    Leader leader = new Leader(employee.name, employee.salary);
                    return leader;
                }).log().subscribe(consumer -> System.out.println(consumer.name));


        System.out.println("=================================== Exception异常 =======================================");

        Flux.range(-2, 5)
                .map(val -> {
                    int i = val / val;
                    return val;
                })
                //遇到错误继续订阅
                .onErrorContinue((ex, val) -> {
                    if (ex instanceof ArithmeticException) {
                        System.out.println("ex:" + ex + ", val:" + val);
                    } else {
                    }
                })
                //遇到错误，返回新的Flux。继续订阅
                .onErrorResume((ex) -> {
                    return Flux.range(-2, 5);
                })
                .subscribe(System.out::println);


        System.out.println("=================================== flatMap映射 =======================================");

        Flux<String> stringFlux3 = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i");
        // 嵌套Flux，2个元素作为一个Flux，存到新的Flux
        Flux<Flux<String>> stringFlux4 = stringFlux3.window(2);
        // flatMap()作用后元素处于单Flux中，可以理解为所有元素平铺
        stringFlux4.flatMap(flux1 -> flux1.map(word -> word.toUpperCase()))
                .subscribe(System.out::println);
        // 从嵌套Flux还原字符串Flux
        Flux<String> stringFlux5 = stringFlux4.flatMap(flux1 -> flux1);
        // stringFlux1 等于 stringFlux5
        stringFlux5.subscribe(System.out::println);


        System.out.println("=================================== concatMap有序映射 =======================================");

        Flux<String> stringFlux6 = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i");
        Flux<Flux<String>> stringFlux7 = stringFlux6.window(2);
        stringFlux7.concatMap(flux1 -> flux1.map(word -> word.toUpperCase())
                .delayElements(Duration.ofMillis(200)))
                .subscribe(x -> System.out.println("->" + x));
        Thread.sleep(2000);


        System.out.println("=================================== collect集合 =======================================");

        Flux<Integer> integerFlux2 = Flux.range(1, 5);
        // 转换成以List<Integer>为对象的Mono
        Mono<List<Integer>> mono = integerFlux2.collectList();
        mono.subscribe(System.out::println);
        Flux<Employee> employeeFlux2 = Flux.fromIterable(EmployeeList);
        // 转换成以List<Employee>为对象的Mono，按薪水排序
        Mono<List<Employee>> mono1 = employeeFlux2.collectSortedList(Comparator.comparing(Employee::getSalary));
        mono1.subscribe(consumer -> consumer.stream().forEach(item -> System.out.println(item.name + ":" + item.salary)));
        // 转换成以Map<String,Employee>为对象的Mono
        Mono<Map<String, Employee>> mono2 = employeeFlux2.collectMap(item -> item.getName(), item -> item);
        mono2.subscribe(System.out::println);


        System.out.println("=================================== take获取 =======================================");

        //根据数量获取
        Flux.range(1, 10).take(0).log().subscribe(System.out::println);
        //根据实际获取
        Flux.range(1, 10000).take(Duration.ofMillis(3)).log().subscribe(System.out::println);
        //根据条件获取
        Flux.range(1, 10).takeUntil(item -> item == 5).log().subscribe(System.out::println);


        System.out.println("=================================== buffer缓冲 =======================================");

        Flux<String> stringFlux8 = Flux.just("a", "b", "c", "d", "e", "f", "g");
        stringFlux8.subscribe(x -> System.out.print("->" + x));
        System.out.println();
        Flux<List<String>> listFlux = stringFlux8.buffer(2);
        listFlux.subscribe(x -> System.out.print("->" + x));
        System.out.println();


        System.out.println("=================================== backpressure背压 =======================================");

        // 每秒产生一个数据
        Flux<Long> longFlux1 = Flux.interval(Duration.ofMillis(1));
        longFlux1.take(10).subscribe(new Subscriber<Long>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long aLong) {
                // 背压，需要三个元素
                subscription.request(3);
                System.out.println("val:" + aLong);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        Thread.sleep(2000);

        System.out.println("=================================== disposable停止Flux流 =======================================");

        Flux<Long> longFlux2 = Flux.interval(Duration.ofMillis(200));
        // take方法准确获取订阅数据量
        Disposable disposable = longFlux2.take(50).subscribe(consumer -> System.out.println(consumer));
        // 主线程休眠1秒后，彻底停止子线程中正在订阅推送数据的Flux或Mono流
        Thread.sleep(1000);
        disposable.dispose();
        System.out.println("stop");


        System.out.println("=================================== parallel多线程 =======================================");

        Flux.range(1, 10)
                .parallel(4)
                .runOn(Schedulers.parallel())
                .subscribe(consumer -> System.out.print("->" + consumer));
        System.out.println();


        System.out.println("=================================== merge合并 =======================================");

        Flux<Long> longFlux11 = Flux.interval(Duration.ofMillis(100)).take(10);
        Flux<Long> longFlux12 = Flux.interval(Duration.ofMillis(100)).take(10);
        Flux<Long> longFlux13 = Flux.merge(longFlux11, longFlux12);
        longFlux13.subscribe(val -> System.out.print(val));
        System.out.println();
        Thread.sleep(2000);


        Thread.sleep(Long.MAX_VALUE);
    }
}
