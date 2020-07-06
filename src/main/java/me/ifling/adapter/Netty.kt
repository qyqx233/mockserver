package me.ifling.adapter;

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import me.ifling.bean.ConfigAdapter
import java.net.InetSocketAddress


class Netty(private val configAdapters: ArrayList<ConfigAdapter>) : PassiveAdapterBase() {
    override fun start() {
        Thread() {
            val bootstrap = ServerBootstrap()
            val boss: EventLoopGroup = NioEventLoopGroup()
            val work: EventLoopGroup = NioEventLoopGroup()
            val channelFutures = ArrayList<ChannelFuture>()
            bootstrap.group(boss, work)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(HttpServerInitializer())

            configAdapters.map {
                channelFutures.add(bootstrap.bind(InetSocketAddress(it.port.toInt())).sync())
            }
            channelFutures.map {
                it.channel().closeFuture().sync()
            }
        }.start()
    }
}
