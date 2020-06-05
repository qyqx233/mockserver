package me.ifling.adapter;


import me.ifling.Context;
import me.ifling.ServiceHandler;
import me.ifling.Util;
import me.ifling.bean.ConfigAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class ActiveAdapterBase implements ActiveAdapter, AdapterRunner {
    ServiceHandler serviceHandler;
    static Logger logger = LoggerFactory.getLogger(ActiveAdapterBase.class);

    public ActiveAdapterBase() {
    }

    public static AdapterRunner getInstance(String name, ConfigAdapter configAdapter) {
        switch (configAdapter.type) {
            case "mq":
                return new MqActiveAdapterImpl(name, configAdapter);
            case "http":
                return new HttpAdapterImpl(name, configAdapter);
        }
        return null;
    }

    public void run() {
        new Thread(() -> {
            try {
                start();
                receiveMsg();
            } catch (Exception e) {
                logger.error(Util.getStackTraceString(e));
            }
        }).run();
    }

    public void handle(String message) {
        try {
            Context ctx = Context.newContext();
            this.serviceHandler.parseRcv(ctx, message);
            this.sendMsg(this.serviceHandler.handle(ctx, message));
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void setServiceHandler(ServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

}

