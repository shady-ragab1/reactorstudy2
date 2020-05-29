package com.shady;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class BlockHound {


    private static void accept(String x) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testAllowed(){

     //   Mono<String> mono = Mono.just("a").doOnNext(BlockHound::accept).subscribeOn(Schedulers.newElastic("elastic"));

       // StepVerifier.create(mono).expectNext("a").verifyComplete();
    }

    @Test
    public void testNotAllowed(){

       // reactor.blockhound.BlockHound.install();

      /*  Mono.delay(Duration.ofSeconds(1))
                .doOnNext(it -> {
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block();*/
    }

}
