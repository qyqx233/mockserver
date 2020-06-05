package me.iflingz

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.toMap
import kotlinx.html.*
import me.ifling.Application
import me.ifling.Config
import me.ifling.service.GroovyScriptDao
import me.ifling.service.IdMapDao
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.Tuple2
import org.slf4j.LoggerFactory

data class IndexData(val items: List<Tuple2<String, String>>)

internal val logger = LoggerFactory.getLogger(Application::class.java)

fun serve(database: Database) {
    val config = Config.getInstance()
    val server = embeddedServer(Netty, port = 8080) {
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
                var name: String = ""
                var id: String = ""

                val params = call.parameters.entries().forEach {
                    if (it.key == "name") {
                        name = it.value[0]
                    }
                    if (it.key == "id") {
                        id = it.value[0]
                    }
                }
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
            get("/script/refresh") {
                Application.getServiceHandlers().map {
                    logger.info("开始刷新groovy {}", it.key)
                    it.value.loadGroovy(null)
                }
            }
            get("/script/new") {
                val map = call.parameters.toMap()
                val name = map["name"]?.get(0)
                val category = map["category"]?.get(0)
                val content = map["content"]?.get(0)
                println("$name $category $content")
                val timestamp = (System.currentTimeMillis() / 1e3).toInt()
                database.update(GroovyScriptDao) {
                    it.content to content
                    it.timestamp to timestamp
                    where {
                        it.category eq category!!
                        it.name eq name!!
                    }
                }.run {
                    if (this == 0) {
                        database.insert(GroovyScriptDao) {
                            it.content to content
                            it.category to category!!
                            it.name to name!!
                            it.timestamp to timestamp
                        }
                    }
                }
                call.respondRedirect("/script", permanent = false)
            }
        }
    }.start(wait = true)
}

fun main() {
    val database = Database.connect("")
    serve(database)
}