package me.ifling.adapter;

public class HttpReq {
    public HttpReq(String url, String method, Object data) {
        this.url = url;
        this.method = method;
        this.data = data;
    }

    public HttpReq(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String url, method;
    public Object data;
}
