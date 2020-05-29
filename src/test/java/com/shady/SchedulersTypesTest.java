package com.shady;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.time.Duration;

public class SchedulersTypesTest {


    //check doucu,emtation schedulers and threading

    @Test
    public void doEveryThingInMainThread() throws InterruptedException {
        Flux.range(0,10).
                log().
                subscribe();

    }

    @Test
    public void doEveryThingInMainThread_delay_opertor() throws InterruptedException {
        Flux.range(0,10).
                log().
                delayElements(Duration.ZERO). //from 1 to 9 emitted in parallel-1
                subscribe();

    }

    @Test
    public void doEveryThingInMainThread_delay_opertor_scheduler_single() throws InterruptedException {
        Flux.range(0,10).
                log().
                delayElements(Duration.ZERO,Schedulers.single()). //from 1 to 9 emitted in parallel-1
                subscribe();

    }

    @Test
    public void blocking_opertor_scheduler_parallel() throws InterruptedException {
        //as per HANDSON_REACTIVE_PROGRAMMING_WITH_REACTOR.pdf parallel schedule shoud throw exception because of the blocking calls
        //parallel number of threads = no of cores
        Flux.range(0,100).
                log().
                delayElements(Duration.ZERO,Schedulers.parallel()). //from 1 to 9 emitted in parallel-1
                window(10). //10 fluxes of 10<blocking>
                subscribe(x-> System.out.println(x.blockFirst()) );



        Thread.sleep(10000l);

    }

    @Test
    public void parallelFlux() throws InterruptedException {

        Flux.range(0,100)
                .log()
                .parallel()
                .runOn(Schedulers.parallel())
                .doOnNext(x->System.out.println("[" +Thread.currentThread().getName()+"]"+"wonderful: "+ x))
                .sequential()
                .doFinally(x->System.out.println("[" +Thread.currentThread().getName()+"]"+"done " +x)) // rin in parallel
                .subscribeOn(Schedulers.single())
                .subscribe();


        Thread.sleep(10000l);

    }

    @Test
    public void cold_ordinary_flux() throws InterruptedException {
        Flux<Integer> ordinary = Flux.range(0,10).log();



        Flux<Integer> connectable = ordinary
                .doFinally(x->System.out.println("done")).replay().autoConnect(2); //onNext called twice

        ordinary.subscribe(System.out::println);
        ordinary.subscribe(System.out::println);

        Thread.sleep(10000l);

    }

    @Test
    public void connectableFlux_broadcasting_replay() throws InterruptedException {
        Flux<Integer> ordinary = Flux.range(0,10).log();

        Flux<Integer> connectable = ordinary
                .doFinally(x->System.out.println("done")).replay().autoConnect(2); //onNext called once amd onComplete too
        connectable.subscribe(System.out::println);
        connectable.subscribe(System.out::println);

        Thread.sleep(10000l);

    }

    @Test
    public void testPublishBroadcast() throws Exception{
        Flux<Long> fibonacciGenerator = Flux.generate(() -> Tuples.<Long,
                Long>of(0L, 1L), (state, sink) -> {
            if (state.getT1() < 0)
                sink.complete();
            else
                sink.next(state.getT1());
            System.out.println("generating next of "+ state.getT2());
            return Tuples.of(state.getT2(), state.getT1() + state.getT2());
        });
        fibonacciGenerator=fibonacciGenerator.doFinally(x -> {
            System.out.println("Closing ");
        }).publish().autoConnect(2);

        //you can see that only the first event is generated. There is no
        //closing event either, because the stream was waiting for the next event request from
        //subscriber one. Consequently, the stream did not terminate.

        fibonacciGenerator.subscribe(new BaseSubscriber<Long>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }
            @Override
            protected void hookOnNext(Long value) {
                System.out.println("1st: "+value);
            }
        });
        fibonacciGenerator.subscribe(x -> System.out.println("2nd : "+x));
        Thread.sleep(500);
    }


}
