package com.bext.reactorbasicbackpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class ReactorAsyncBackpressureDrop {
    private static final Logger LOG = LoggerFactory.getLogger(ReactorAsyncBackpressureDrop.class);

    public static void main(String... args) throws InterruptedException {
        Flux<Object> fluxBackpressure = Flux.create(emitter -> {
            //Publish 1000 numbers
            IntStream intStream = IntStream.range(1, 1000);
            intStream.forEach(i -> {
                LOG.info("{} | Publishing: {}", Thread.currentThread().getName(), i);
                emitter.next(i);
            });
            emitter.complete();
        }, FluxSink.OverflowStrategy.DROP)
                .onBackpressureDrop(i -> LOG.warn("{} | Dropped: {}", Thread.currentThread().getName(), i));

        fluxBackpressure.subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic()).subscribe(i -> {
            //process received value
            LOG.info("{} | Received: {}", Thread.currentThread().getName(), i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(20000); // enough time to receive the 256 publishes not Dropped
    }

}
