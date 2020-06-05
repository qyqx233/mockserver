package me.ifling.service

import groovy.lang.GroovyObject
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import me.ifling.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset
import java.sql.SQLException
import kotlin.math.ceil

internal open class A(val name: String?)

internal class AA(name: String?) : A(name)


class Identify(name: String) : ServiceHandlerImpl(name) {

    private val logger: Logger = LoggerFactory.getLogger(Identify::class.java)
    override fun isObject(): Boolean {
        return false
    }

    override fun prepareDB(database: Database) {
        database.useConnection { it ->
            val sqlArray = arrayOf(
                    "CREATE TABLE IF NOT EXISTS identify_map(\n" +
                            "id varchar(18) PRIMARY KEY,\n" +
                            "name varchar(100) NOT NULL,\n" +
                            "photo varchar(1000000)\n" +
                            ")".trimIndent(),
                    "CREATE TABLE IF NOT EXISTS groovy_script(\n" +
                            "id integer PRIMARY KEY autoincrement,\n" +
                            "category varchar(100) NOT NULL,\n" +
                            "name varchar(100),\n" +
                            "content varchar(10000),\n" +
                            "timestamp int\n" +
                            ");".trimIndent())
            try {
                it.createStatement().use { stmt ->
                    sqlArray.map {
                        println("开始执行sql语句: $it")
                        stmt.execute(it)
                    }
                }
                val id = "320621200001010001"
                val name = "张三"
                database.from(IdMapDao).select().where {
                    (IdMapDao.id eq id) and (IdMapDao.name eq name)
                }.run {
                    if (this.totalRecords == 0) {
                        database.insert(IdMapDao) {
                            it.name to name
                            it.id to id
                        }
                    }
                }

            } catch (e: SQLException) {
                logger.error(Util.getStackTraceString(e))
            }
        }
    }

    override fun handle(ctx: Context, message: String): String {
        val clazz = this.groovyScriptMap["handle"]?.clazz
        val config = Config.getInstance()
        val parser = XMLParser(message)
        val reqMap = mutableMapOf<String, String?>(
                "app" to parser.selectSingleNode("//APP")?.text,
                "id" to parser.selectSingleNode("//ID")?.text,
                "name" to parser.selectSingleNode("//Name")?.text,
                "entrustDate" to parser.selectSingleNode("//EntrustDate")?.text,
                "msgNo" to parser.selectSingleNode("//MsgNo")?.text,
                "msgID" to parser.selectSingleNode("//MsgID")?.text,
                "msgRef" to parser.selectSingleNode("//MsgRef")?.text,
                "workDate" to parser.selectSingleNode("//WorkDate")?.text,
                "src" to parser.selectSingleNode("//SRC")?.text,
                "des" to parser.selectSingleNode("//DES")?.text
        )
        if (reqMap["id"] == null || reqMap["name"] == null) {
            return String(Identify::class.java.classLoader.getResource("static/id-failed.xml")
                    .openStream().readAllBytes(), Charset.forName("utf8"))
        }
        return Application.database.from(IdMapDao).select().where {
            (IdMapDao.id eq (reqMap["id"] ?: error(""))) and (IdMapDao.name eq (reqMap["name"] ?: error("")))
        }.run {
            if (this.totalRecords == 1) {
                reqMap["checkResult"] = "00"
            } else {
                reqMap["checkResult"] = "01"
            }

            (clazz?.getConstructor()?.newInstance() as GroovyObject).invokeMethod("handle", arrayOf<Any>(parser, ctx, reqMap))
            FreeMarker.render("query.ftl", reqMap)
        }
    }

    override fun handleObject(ctx: Context, obj: Any): Any {
        TODO("Not yet implemented")
    }

    override fun registerRouter(server: ApplicationEngine) {
        server.application.routing {
            get("/id") {

                call.respondText("HELLO WORLD!")

            }
            get("/ids") {
//                val query = database.from(PostID).select()
            }
            post("/id") {
//                val postID = call.receive<PostID>()
//                database.execute("insert into identify_map () values()")
                call.respondText("")
            }
        }
    }

    override fun parseRcv(ctx: Context, message: String) {
        ctx.url = ""
    }

    override fun parseRcvObject(ctx: Context, message: Any) {
        TODO("Not yet implemented")
    }


}

fun main() {
    println("{}".format(1))
    var s: String? = null
    println(s?.length ?: -1)
    val h = Identify("")
    val ctx = Context.newContext()
    val reqMap = mutableMapOf<String, String?>(
            "id" to "312323",
            "app" to "APP",
            "name" to "张三",
            "entrustDate" to "2020051912312",
            "msgNo" to "1231aasd",
            "msgID" to "123123123",
            "msgRef" to "111111111",
            "workDate" to "20200519",
            "src" to "from",
            "des" to "to"
    )
    val ss = FreeMarker.render("query-req.ftl", reqMap)
    println(ss)
    println("==================")
    println("sb".hashCode())
    println("sb".hashCode())

    val bs = byteArrayOf('s'.toByte(), 'b'.toByte())
    println(String(bs).hashCode())
    File(".")
    println(ceil(System.currentTimeMillis() / 1e3))
    print(System.currentTimeMillis().toInt())
}