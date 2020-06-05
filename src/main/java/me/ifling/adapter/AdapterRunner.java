package me.ifling.adapter;

import me.ifling.ServiceHandler;

public interface AdapterRunner {
    public void run();
    public void start() throws Exception;
    public void setServiceHandler(ServiceHandler serviceHandler);
}
