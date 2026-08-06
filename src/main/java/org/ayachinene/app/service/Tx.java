package org.ayachinene.app.service;

import java.util.concurrent.Callable;

public interface Tx {

    <T> T run(Callable<T> action);

    void run(Runnable action);
}
