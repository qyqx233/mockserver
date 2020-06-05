package me.ifling

import groovy.lang.GroovyObject
import io.ktor.server.engine.ApplicationEngine
import me.liuwj.ktorm.database.Database

class GroovyService(name: String) : ServiceHandlerImpl(name) {
    override fun isObject(): Boolean {
        return false
    }

    override fun prepareDB(database: Database) {
    }

    override fun handle(ctx: Context, message: String): String {
        val clazz = this.groovyScriptMap["service"]?.clazz
        val parser = XMLParser(message)
        return (clazz?.getConstructor()?.newInstance() as GroovyObject)
                .invokeMethod("handle", arrayOf<Any>(ctx, message, parser)) as String
    }

    override fun handleObject(ctx: Context, obj: Any): Any {
        TODO("Not yet implemented")
    }

    override fun parseRcv(ctx: Context, message: String) {
        val clazz = this.groovyScriptMap["service"]?.clazz
        (clazz?.getConstructor()?.newInstance() as GroovyObject)?.invokeMethod("parseRcv", arrayOf<Any>(ctx, String))
    }

    override fun parseRcvObject(ctx: Context, arg: Any) {

    }

    override fun registerRouter(server: ApplicationEngine) {
    }
}