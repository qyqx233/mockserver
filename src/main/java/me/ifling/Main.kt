package me.ifling

import com.fasterxml.jackson.databind.SerializationFeature
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.toMap
import kotlinx.html.*
import me.ifling.service.GroovyScriptDao
import me.ifling.service.IdMapDao
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import org.slf4j.LoggerFactory
import java.util.*

data class NullJson(val ver: String="")

data class GroovyScriptJson(val name: String, val category: String, var content: String?, val names: List<String>?=null,
                            val method: String?="")

data class ResponseJson(var code: Int, var data: Object?)
data class MockJson(val category: String, val content: String)

object Main {}

internal val logger = LoggerFactory.getLogger(Main::class.java)

fun serve(database: Database) {
    val config = Config.getInstance()
    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT) // Pretty Prints the JSON
            }
        }
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        }
        routing {
            static("/static") {
                resources("static")
            }
            get("/identify") {
                this::class.java.classLoader.getResource("static/id.html").openStream().use { inputStream ->
                    call.respondText(String(inputStream.readAllBytes()), ContentType.Text.Html)
                }
            }
            post("/api/service/list") {
                call.receive<NullJson>()
                call.respond(ResponseJson(0, Config.getInstance().services.map {
                    it.key
                }.toList() as Object))
            }
            get("/identify/query") {
                val list = database.from(IdMapDao).select()
                call.respondHtml {
                    head {
                        link(rel = "stylesheet", href = "/static/pure-min.css")
                        title("查询")
                    }
                    body {
                        table(classes = "pure-table pure-table-bordered") {
                            for ((i, row) in list.withIndex()) {
                                tr(if (i % 2 == 0) "pure-table-odd" else "") {
                                    td {
                                        +"${row[IdMapDao.id]}"
                                    }
                                    td {
                                        +"${row[IdMapDao.name]}"
                                    }
                                }
                            }
                        }
                    }
                }
            }
            get("/identify/update") {
                val map = call.parameters.toMap()
                var name: String = map["name"]?.get(0) ?: ""
                var id: String = map["id"]?.get(0) ?: ""

                if (name == "" || id == "") {
                    call.respondRedirect("/identify", permanent = false)
                    return@get;
                }
                println("$name, $id")
                database.update(IdMapDao) {
                    it.name to name
                    where {
                        it.id eq id
                    }
                }.run {
                    if (this == 0) {
                        database.insert(IdMapDao) {
                            it.name to name
                            it.id to id
                        }
                    }
                }
                call.respondRedirect("/identify", permanent = false)
            }
            get("/script") {
                this::class.java.classLoader.getResource("static/script.html").openStream().use { inputStream ->
                    call.respondText(String(inputStream.readAllBytes()), ContentType.Text.Html)
                }
            }
            post("/script/category/list") {
                val req = call.receive<GroovyScriptJson>()
                val names = ArrayList<String>()
                val groovyScriptJson = GroovyScriptJson(req.name, req.category, "", names)
                val resp = ResponseJson(0, groovyScriptJson as Object)
                database.useConnection exit@{ it ->
                    it.createStatement().use {
                        val sql = "select distinct name from groovy_script where category = '${req.category}'"
                        logger.info(sql)
                        val rs = it.executeQuery(sql)
                        rs.use {
                            while (rs.next()) {
                                println(rs.getString(1))
                                names.add(rs.getString(1))
                            }
                        }
                    }
                }
                call.respond(resp)
            }
            post("/script/query") {
                val req = call.receive<GroovyScriptJson>()
                val rsp = GroovyScriptJson(req.name, req.category, "", null, "")
                database.from(GroovyScriptDao).select().where {
                    GroovyScriptDao.category eq req.category
                    GroovyScriptDao.name eq req.name
                }.map {
                    rsp.content = it[GroovyScriptDao.content]
                    call.respond(ResponseJson(0, rsp as Object))
                    return@post;
                }
                call.respond(ResponseJson(100, rsp as Object))
            }
            get("/script/refresh") {
                Application.getServiceHandlers().map {
                    logger.info("开始刷新groovy {}", it.key)
                    val bio = StringBuilder()
                    it.value.loadGroovy(bio)
                    call.respondText(bio.toString(), ContentType.Text.Html)
                }
            }
            post("/script/update") {
                val req = call.receive<GroovyScriptJson>()
                if (req.method == "delete") {
                    database.delete(GroovyScriptDao) {
                        it.category eq req.category
                        it.name eq req.name
                    }
                    call.respond(ResponseJson(0, null))
                    return@post
                }
                val timestamp = (System.currentTimeMillis() / 1e3).toInt()
                database.update(GroovyScriptDao) {
                    it.content to req.content
                    it.timestamp to timestamp
                    where {
                        it.category eq req.category
                        it.name eq req.name
                    }
                }.run {
                    if (this == 0) {
                        database.insert(GroovyScriptDao) {
                            it.content to req.content
                            it.category to req.category
                            it.name to req.name
                            it.timestamp to timestamp
                        }
                    }
                }
                Application.getServiceHandlers().filter {
                    it.key == req.category
                }.map {
                    it.value.loadGroovy(null)
//                    this[req.name] = GroovyClassMeta(timestamp)
                }
                call.respond(ResponseJson(0, null))
            }
            get("mock") {
                this::class.java.classLoader.getResource("static/mock.html").openStream().use { inputStream ->
                    call.respondText(String(inputStream.readAllBytes()), ContentType.Text.Html)
                }
            }
            post("mock") {
                val req = call.receive<MockJson>()
                val ctx = Context.newContext()
                logger.info(req.content)
                logger.info(Application.getServiceHandlers().toString())
                val response = ResponseJson(0, MockJson(req.category,
                        Application.getServiceHandlers()[req.category]?.handle(ctx, req.content)!!) as Object)
                call.respond(response)
            }
        }
    }.start(wait = true)
}

fun main() {
    val config = Config.getInstance()
    Initialization.prepareApplication()
    Application.database.useConnection { it.autoCommit = false }
    Initialization.prepareDBGroovy(config, Application.database)
    serve(Application.database)
}