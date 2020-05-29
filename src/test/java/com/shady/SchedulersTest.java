package com.shady;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class SchedulersTest {


    //check doucu,emtation schedulers and threading

    @Test
    public void publishOn() throws InterruptedException {

        Scheduler s = Schedulers.newParallel("parallel",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .publishOn(s)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }

    @Test
    public void subscribeOn() throws InterruptedException {

        Scheduler s = Schedulers.newParallel("parallel",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .subscribeOn(s)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }

    @Test
    public void subscribeOnPublishOn() throws InterruptedException {

        Scheduler s1 = Schedulers.newParallel("parallel1",4);
        Scheduler s2 = Schedulers.newParallel("parallel2",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .subscribeOn(s1)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .publishOn(s2)
                .map(x->{
                    System.out.println("3 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }

    @Test
    public void publishOnSubscribeOn() throws InterruptedException {

        Scheduler s1 = Schedulers.newParallel("parallel1",4);
        Scheduler s2 = Schedulers.newParallel("parallel2",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .publishOn(s1)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .subscribeOn(s2)
                .map(x->{
                    System.out.println("3 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }

    @Test
    public void subscribeOnSubscribeOn() throws InterruptedException {

        //only first subscribe on in the chain is effective, so all in parallel1

        Scheduler s1 = Schedulers.newParallel("parallel1",4);
        Scheduler s2 = Schedulers.newParallel("parallel2",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .subscribeOn(s1)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .subscribeOn(s2)
                .map(x->{
                    System.out.println("3 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }


    @Test
    public void publishOnPublishOn() throws InterruptedException {

        //only first subscribe on in the chain is effective, so all in parallel1

        Scheduler s1 = Schedulers.newParallel("parallel1",4);
        Scheduler s2 = Schedulers.newParallel("parallel2",4);

        Flux<Integer> flux = Flux
                .range(0,1)
                .map(x ->{
                    System.out.println("1 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .publishOn(s1)
                .map(x->{
                    System.out.println("2 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                })
                .publishOn(s2)
                .map(x->{
                    System.out.println("3 map: "+ Thread.currentThread().getName() + " " + x);
                    return x+x;
                });

        Thread t =new Thread(()->{
            flux.subscribe(x-> {
                System.out.println("subscription: "+ Thread.currentThread().getName() + " " + x);

            });
        });

        t.setName("anonymous");

        t.start();
        t.join();
    }
}
