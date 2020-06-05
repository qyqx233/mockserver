package me.ifling.adapter;

import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.ifling.bean.ConfigAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


class Param {
    public String getUrl() {
        return url;
    }

    private String url;

    public String getMethod() {
        return method;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }

    private String method;

    public void setData(byte[] data) {
        this.data = data;
    }

    private byte[] data;
    private String contentType;

    public Param(String url, String method, byte[] data, String contentType) {
        this.url = url;
        this.method = method;
        this.data = data;
        this.contentType = contentType;
    }
}

public class HttpAdapterImpl extends PassiveAdapterBase {
    private String name;
    private ConfigAdapter configAdapter;
    private HttpServer server;

    private static Logger logger = LoggerFactory.getLogger(PassiveAdapterBase.class);

    public HttpAdapterImpl(String name, ConfigAdapter configAdapter) {
        this.name = name;
        this.configAdapter = configAdapter;
    }

    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(Integer.parseInt(configAdapter.port)), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(200, 0);
                String method = httpExchange.getRequestMethod();
                Param param = new Param(httpExchange.getRequestURI().toString(), httpExchange.getRequestMethod(),
                        null, httpExchange.getRequestHeaders().getFirst("Content-type"));
                if (method.equals("POST")) {
                    InputStream is = httpExchange.getRequestBody();
                    param.setData(is.readAllBytes());
                }
                Headers headers = httpExchange.getRequestHeaders();
                Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
                ArrayList result = (ArrayList) handleObject(param);
                String contentType = (String) result.get(0);
                Object resultObj = result.get(1);
                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", contentType);
                logger.info(contentType);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    if (resultObj instanceof String) {
                        os.write(((String) resultObj).getBytes());
                    } else if (resultObj instanceof byte[]) {
                        os.write((byte[]) resultObj);
                    } else {
                        os.write(JSON.toJSONBytes(resultObj));
                    }
                }
            }
        });
        server.start();
    }

    public static void main(String[] args) throws Exception {
        Object obj = "ss";
        boolean b;
        ConfigAdapter configAdapter = new ConfigAdapter();
        configAdapter.port = "4321";
        new HttpAdapterImpl("", configAdapter).start();
    }
}
