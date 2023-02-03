package org.trading.krakenapi.threading;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;

@Component
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
