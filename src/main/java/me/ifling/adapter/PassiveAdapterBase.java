package me.ifling.adapter;

import me.ifling.Context;
import me.ifling.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PassiveAdapterBase implements PassiveAdapter, AdapterRunner {
    public void setServiceHandler(ServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    private ServiceHandler serviceHandler;
    private static Logger logger = LoggerFactory.getLogger(PassiveAdapterBase.class);

    public void run() {
        new Thread(() -> {
            try {
                start();
            } catch (Exception e) {
                logger.error("", e);
            }
        }).run();
    }

    public Object handleObject(Object object) {
        Context ctx = Context.newContext();
        serviceHandler.parseRcvObject(ctx, object);
        return serviceHandler.handleObject(ctx, object);
    }
}
