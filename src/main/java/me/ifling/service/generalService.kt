package me.ifling.service

import groovy.lang.GroovyObject
import io.ktor.server.engine.ApplicationEngine
import me.ifling.Context
import me.ifling.ServiceHandlerImpl
import me.liuwj.ktorm.database.Database
import org.slf4j.LoggerFactory


//internal class HttpAdapterImpl : PassiveAdapterBase() {
//    private val name: String? = null
//    private val configAdapter: ConfigAdapter? = null
//    private val server: HttpServer? = null
//
//    companion object {
//        private val logger: Logger = LoggerFactory.getLogger(PassiveAdapterBase::class.java)
//    }
//}

class GeneralService(name: String) : ServiceHandlerImpl(name) {
    companion object {
        private val logger = LoggerFactory.getLogger(GeneralService::class.java)
    }

    override fun isObject(): Boolean {
        return true
    }

    override fun prepareDB(database: Database) {
    }

    override fun handle(ctx: Context, message: String): String {
        TODO("Not yet implemented")
    }

    override fun handleObject(ctx: Context, arg: Any): Any {
        val clazz = this.groovyScriptMap["service"]?.clazz
        return (clazz?.getConstructor()?.newInstance() as GroovyObject)?.invokeMethod("handleObject",
                arrayOf(ctx, arg))
    }

    override fun parseRcv(ctx: Context, message: String) {
        TODO("Not yet implemented")
    }

    override fun parseRcvObject(ctx: Context, arg: Any) {
        val clazz = this.groovyScriptMap["service"]?.clazz
//        (clazz?.getConstructor()?.newInstance() as GroovyObject)?.invokeMethod("parseRcvObject", arrayOf<Any>(ctx, arg))
    }

    override fun registerRouter(server: ApplicationEngine) {
        TODO("Not yet implemented")
    }

}