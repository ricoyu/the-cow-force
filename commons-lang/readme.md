# 一 限流

```java
SlidingWindow slidingWindow = RateLimits.slidingTimeWindow(10L, SECONDS)
        .limit(10l)
        .precision(10)
        .build();

if (slidingWindow.canPass()) {
 	.... 
}
```

