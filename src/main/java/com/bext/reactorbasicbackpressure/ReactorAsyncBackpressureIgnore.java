package com.bext.reactorbasicbackpressure;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class ReactorAsyncBackpressureIgnore {
    private static final Logger LOG = LoggerFactory.getLogger(ReactorAsyncBackpressureIgnore.class);

    public static void main(String[] args) throws InterruptedException {

        Flux<Object> fluxBackpressure = Flux.create(emitter -> {
            //Publish 1000 numbers
            IntStream intStream = IntStream.range(1, 1000);
            intStream.forEach(i -> {
                LOG.info("{} | Publishing: {}", Thread.currentThread().getName(), i);
                emitter.next(i);
                try {
                    Thread.sleep(1 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            emitter.complete();
        }, FluxSink.OverflowStrategy.IGNORE);

        fluxBackpressure.subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic()).subscribe(i -> {
            //process received value
            LOG.info("{} | Received: {}", Thread.currentThread().getName(), i);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, err -> {
            LOG.error("{} | Error : {}", Thread.currentThread().getName(), err.getClass().getSimpleName() + " " + err.getMessage());
        });
        Thread.sleep(20000);
    }
}
