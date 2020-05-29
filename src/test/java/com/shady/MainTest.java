package com.shady;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MainTest {

    private static List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

@Test
public void operators(){

   Flux.fromIterable(words).
           flatMap(word ->Flux.fromArray(word.split("")) ).
           concatWith(Mono.just("s")).
           distinct().
           sort().
           zipWith(Flux.range(1,27),(l,c)->c + "." + l).subscribe(System.out::println);
}


@Test
public void noTime(){

    Mono.just("Hello").concatWith(Mono.just(" ")).concatWith(Flux.just("world")).delaySubscription(Duration.ofMillis(500))
    .subscribe(System.out::println);

}

    @Test
    public void noTime_blockIT(){

        Mono.just("Hello").concatWith(Mono.just(" ")).concatWith(Flux.just("world"))
               .delaySubscription(Duration.ofMillis(5000))
               .toStream().forEach(System.out::print);


    }

    @Test
    public void firstEmitting(){

        Flux<String> flux = Flux.just("a","b");

        Mono<String> mono = Mono.just("a").delaySubscription(Duration.ofSeconds(30));

        StepVerifier.create(flux)
                .expectNext("a", "b")
                .expectComplete()
                .verify();

        StepVerifier.create(flux)
                .consumeNextWith(c -> assertThat(c).as("alphapetic").matches("[a-z]") )
                .consumeNextWith(c -> assertThat(c).as("alphapetic").matches("[a-z]") )
                .expectComplete()
                .verify();


        StepVerifier
                .withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(1L)
                .verifyComplete();


    }

    @Test
    public void errorHandle(){
        Flux.just(10,11).map(x -> x/0).onErrorReturn(-1).subscribe(System.out::println); //static value
        Flux.just(10).map(x -> x/0).onErrorResume(throwable -> Flux.just(-1,-1,-1,-1)).subscribe(System.out::print); //fallback to another stream

    }

    @Test
    public void mapError(){
        Flux.just(10,11).map(x -> x/0).onErrorMap(e->{throw new RuntimeException("nothing serious");}).subscribe(System.out::println,e->e.printStackTrace());
    }



}
