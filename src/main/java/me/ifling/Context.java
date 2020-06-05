package me.ifling;

public class Context {
    private static int serial;
    public String url;

    public static Context newContext() {
        Context ctx = new Context();
        return ctx;
    }

    void handle(String adapterName) {
        Context ctx = new Context();
    }

    private Context() {

    }

}
