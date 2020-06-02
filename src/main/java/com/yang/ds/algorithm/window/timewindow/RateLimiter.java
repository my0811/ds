package com.yang.ds.algorithm.window.timewindow;

public interface RateLimiter {
    boolean isOverLimit();

    int currentQPS();

    boolean visit();

}
