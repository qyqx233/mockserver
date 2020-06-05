package me.ifling

import io.ktor.server.engine.ApplicationEngine
import me.ifling.service.GroovyScriptDao
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ServiceHandler {
    fun isObject(): Boolean
    fun prepareDB(database: Database)
    fun handle(ctx: Context, message: String): String
    fun handleObject(ctx: Context, obj: Any): Any
    fun parseRcv(ctx: Context, message: String)
    fun parseRcvObject(ctx: Context, arg: Any)
    fun registerRouter(server: ApplicationEngine)
    fun loadGroovy(bio: java.lang.StringBuilder?)
}

data class GroovyClassMeta(val timestamp: Int, val clazz: Class<*>)

open abstract class ServiceHandlerImpl(val name: String) : ServiceHandler {
    private val logger: Logger = LoggerFactory.getLogger(Application::class.java)
    protected val groovyScriptMap = HashMap<String, GroovyClassMeta>()

    override fun loadGroovy(bio: StringBuilder?) {
        Application.database.from(GroovyScriptDao).select().where {
            GroovyScriptDao.category eq name
        }.map {
            val content = it[GroovyScriptDao.content]!!
            val name = it[GroovyScriptDao.name]!!
            val category = it[GroovyScriptDao.category]!!
            val timestamp = it[GroovyScriptDao.timestamp]!!
            val data = groovyScriptMap[name]
            if ((data != null && timestamp > data.timestamp) || data == null) {
                if (bio != null) {
                    bio?.append("加载groovy category=$category, name=$name<p>\n")
                } else {
                    logger.info("加载groovy category=$category, name=$name")
                }
                groovyScriptMap[name] = GroovyClassMeta(timestamp,
                        GroovyScriptClassCache.newInstance().loadScript(content)!!)
            }
        }
    }
}