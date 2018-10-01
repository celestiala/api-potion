package com.celestiala.apipotion.core.async;

public interface AsyncWorker {
    void submitAsync(Runnable run);
}
