package org.trading.krakenapi.runner;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.trading.krakenapi.manager.KrakenConnectionManager;

@Component
public class KrakenApplicationRunner implements ApplicationRunner {
    private final AutowireCapableBeanFactory beanFactory;

    public KrakenApplicationRunner(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        KrakenConnectionManager krakenConnectionManager = new KrakenConnectionManager("", "");
        beanFactory.autowireBeanProperties(krakenConnectionManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }
}
