/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify;

import java.time.Duration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

/**
 *
 * @author admin
 */
public class ReactiveSample {

    public static void main(String[] args) throws Exception{
        EmitterProcessor<Long> emit = EmitterProcessor.<Long>create();
//        emit.publishOn(Schedulers.elastic());
//        emit.onNext("Replay");
        emit.subscribe(v -> System.out.println("first: " +v + " on thread "+ Thread.currentThread().getName()));
        emit.delayElements(Duration.ofSeconds(1)).subscribe(v -> System.out.println("second: " +v + " on thread "+ Thread.currentThread().getName()));
        emit.subscribe(v -> System.out.println("third: " +v + " on thread "+ Thread.currentThread().getName()));
        emit.subscribe(v -> System.out.println("fourth: " +v + " on thread "+ Thread.currentThread().getName()));
        emit.subscribe(v -> System.out.println("fifth: " +v + " on thread "+ Thread.currentThread().getName()));
        
        
        Flux.interval(Duration.ofSeconds(3))
                .subscribe(v -> emit.onNext(v));
        Thread.sleep(500000000);

//        emit.next();
    }
}
