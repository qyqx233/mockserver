package me.ifling.adapter;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import me.ifling.Application;
import me.ifling.Context;
import me.ifling.Util;
import me.ifling.service.HTTPService;
import me.ifling.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;


public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private static class Holder {
        private static HTTPService httpService = (HTTPService) Application.INSTANCE.getServiceHandlers().get("http");
        private static Map<Integer, String> httpAdapters = Application.INSTANCE.getHttpAdapters();
    }

    public HttpRequestHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //100 Continue
        // 获取请求的uri
        FullHttpRequest req = (FullHttpRequest) msg;
        HttpRequest httpReq = new HttpRequest(req.getUri(), req.getMethod().toString());
        String uri = req.getUri();
        java.net.InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
        if (req.getMethod() == HttpMethod.POST) {
            ByteBuf buf = req.content();
            httpReq.data = JSON.parse(buf.toString(CharsetUtil.UTF_8));

        }
        FullHttpResponse response = null;
        try {
            ArrayList responseObject = (ArrayList) Holder.httpService.handleObject(
                    Context.newContext(Holder.httpAdapters.get(address.getPort())), httpReq);
            Object responseData = responseObject.get(1);
            Supplier<ByteBuf> fx = (() -> {
                if (responseData instanceof String) {
                    return Unpooled.copiedBuffer((String) responseData, CharsetUtil.UTF_8);
                } else if (responseData instanceof byte[]) {
                    return Unpooled.copiedBuffer((byte[]) responseData);
                } else {
                    return Unpooled.copiedBuffer(JSON.toJSONString(responseData), CharsetUtil.UTF_8);
                }
            });
            response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    fx.get());
            try {
                LinkedHashMap<String, String> responseHeaders = (LinkedHashMap<String, String>) responseObject.get(0);
                Iterator<Map.Entry<String, String>> iterator = responseHeaders.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    logger.info("{} {}", entry.getKey(), entry.getValue());
                    response.headers().set(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    Unpooled.copiedBuffer(e.getMessage() + ": \n\n" + Util.getStackTraceString(e), CharsetUtil.UTF_8));
        }

        // 将html write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void main(String[] args) {
        Object[] argx = new Object[2];
        argx[0] = "";
        Utils.INSTANCE.getTemplate("");
    }
}