package com.example.ertebatfardaboarding.service.rateLimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private ConcurrentHashMap<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    public Bucket createBucket(String user) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1))))
                .build();
    }

    public Bucket getBucket(String user) {
        return bucketMap.computeIfAbsent(user, this::createBucket);
    }

    public boolean tryConsume(String user) {
        Bucket bucket = getBucket(user);
        return bucket.tryConsume(1);
    }

}
