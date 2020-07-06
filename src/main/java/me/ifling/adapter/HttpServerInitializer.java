package me.ifling.adapter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private int port = 0;
    private String name = null;
    private static final Logger logger = LoggerFactory.getLogger(HttpServerInitializer.class);

    public HttpServerInitializer() {
    }

    public HttpServerInitializer(int port, String name) {
        this.port = port;
        this.name = name;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());// http 编解码
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024)); // http 消息聚合器                                                                     512*1024为接收的最大contentlength
        pipeline.addLast("handler", new HttpRequestHandler());// 请求处理器

    }
}