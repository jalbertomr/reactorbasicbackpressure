package com.bext.reactorbasicbackpressure;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class ReactorAsyncBackpressureIgnore {
    public static void main(String[] args) throws InterruptedException {
        Flux<Object> fluxBackpressure = Flux.create(emitter -> {
            //Publish 1000 numbers
            IntStream intStream = IntStream.range(1, 1000);
            intStream.forEach(i -> {
                System.out.println(Thread.currentThread().getName() + "| Publishing = " + i);
                emitter.next(i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            emitter.complete();
        }, FluxSink.OverflowStrategy.IGNORE);

        fluxBackpressure.subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic()).subscribe(i -> {
            //process received value
            System.out.println(Thread.currentThread().getName() + " | Received = " + i);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, err -> {
            System.err.println(Thread.currentThread().getName() + " | Error "
                    + err.getClass().getSimpleName() + " " + err.getMessage());
        });
        Thread.sleep(100000);
    }
}
