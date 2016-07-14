package com.tmoncorp.apipotion.core.async;

public interface AsyncWorker {
    void submitAsync(Runnable run);
}
