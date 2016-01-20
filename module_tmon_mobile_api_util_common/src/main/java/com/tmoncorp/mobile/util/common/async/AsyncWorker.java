package com.tmoncorp.mobile.util.common.async;

public interface AsyncWorker {
    void submitAsync(Runnable run);
}
