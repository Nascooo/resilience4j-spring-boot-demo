package com.nasco.circuitbreaker.controller;

import com.nasco.circuitbreaker.model.User;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RequestMapping("/api")
@RestController
@Slf4j
public class CircuitBreakerController {


    @Autowired
    private RestTemplate restTemplate;

    private static final String CIRCUIT_BREAKER_NAME = "poc";

    private Integer retriesNumber = 0;


    @GetMapping("/timeLimiter/{timeout}")
    @TimeLimiter(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMono")
    public Mono<List<User>> getUsersTimeLimiter(@PathVariable int timeout) {
        ResponseEntity<User[]> forEntity = restTemplate.getForEntity("http://localhost:8081/user/", User[].class);
        return Mono.just(Arrays.asList(Objects.requireNonNull(forEntity.getBody()))).delayElement(Duration.ofSeconds(timeout));
    }

    @GetMapping("/retry/{timeout}")
    @Retry(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallback")
    public List<User> getUsersRetry(@PathVariable int timeout) throws Exception {
        Thread.sleep(timeout * 1000);
        System.out.println("Retry Method Called");
        retriesNumber++;
        return Arrays.asList(restTemplate.getForEntity("http://localhost:8081/users/", User[].class).getBody());
    }

    private Mono<List<User>> fallbackMono(Exception ex) {
        return Mono.just(Arrays.asList(new User()))
                .doOnNext(result -> log.warn("fallback executed"));
    }

    private List<User> fallback(Exception ex) {
        System.out.println("Retried for " + retriesNumber);
        User user = new User();
        user.setResponseMessage("Error Number Of Retries Exceeded");
        user.setCode(500);
        return Arrays.asList(user);
    }


}
