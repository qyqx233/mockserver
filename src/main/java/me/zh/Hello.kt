package me.zh

import me.ifling.Application
import me.ifling.service.IdMapDao
import me.ifling.service.Identify
import me.liuwj.ktorm.dsl.*
import me.zh.xml.ReadXMLByDOMWithXpath
import java.nio.charset.Charset
import javax.script.ScriptEngineManager

data class MessageBody(val name: String)
data class Message(val body: MessageBody)

internal interface YY {
    fun show(): String?
}


internal fun world(f: (YY)) {
    f.show()
}

fun main() {
    fun foo() {
        listOf(1, 2, 3, 4, 5).forEach lit@{
            if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
            print(it)
        }
        print(" done with explicit label")
    }
    foo()

    ReadXMLByDOMWithXpath::class.java.classLoader.getResource("static/id.html").openStream().use { inputStream ->
        inputStream.readAllBytes()
    }

    println(String(Identify::class.java.classLoader.getResource("static/id-failed.xml")
            .openStream().readAllBytes(), Charset.forName("utf8")))
    val query = Application.database.from(IdMapDao).select().where {
        (IdMapDao.id eq "324") and (IdMapDao.name eq "333")
    }.run {
        if (this.totalRecords == 1) {

        }
    }
    println(Application::class.java.classLoader.getResource("templates/query.ftl").path)
//    val cfg = Configuration()
//    cfg.templateLoader = ClassTemplateLoader(Application::class.java.classLoader, "templates")
//    cfg.defaultEncoding = "UTF-8"
//    cfg.locale = Locale.SIMPLIFIED_CHINESE
//    val template = cfg.getTemplate("query.ftl")
//    val bio = ByteArrayOutputStream()
//    val consoleWriter: Writer = OutputStreamWriter(bio)
//    template.process(mapOf("data" to Message(MessageBody("aa")), "title" to "title"), consoleWriter)
//    println(bio.toString()
//    FreeMarker.render("query.ftl", mapOf("data" to Message(MessageBody("aa")), "title" to "title")).run {
//        println(this)
//    }
//    org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
    val manager = ScriptEngineManager()
    manager.engineFactories.forEach { println(it.extensions) }
    with(ScriptEngineManager().getEngineByExtension("kts")) {
        print(this)
        eval("val x = 3")
        val res2 = eval("x + 2")
    }
}

