package me.ifling

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import me.ifling.adapter.ActiveAdapterBase
import me.ifling.adapter.AdapterRunner
import me.ifling.bean.ConfigAdapter
import me.ifling.bean.ConfigService
import me.liuwj.ktorm.database.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.util.*
import kotlin.collections.HashMap

object FreeMarker {
    private val cfg: Configuration = Configuration()

    init {
        cfg.templateLoader = ClassTemplateLoader(Application::class.java.classLoader, "templates")
        cfg.defaultEncoding = "UTF-8"
        cfg.locale = Locale.SIMPLIFIED_CHINESE
    }

    fun render(templateName: String, data: Any): String {
        val template = cfg.getTemplate(templateName)
        val bio = ByteArrayOutputStream()
        val writer: Writer = OutputStreamWriter(bio)
        template.process(data, writer)
        return bio.toString()
    }
}

object Application {
    private val serviceHandlers: Map<String, ServiceHandler> = HashMap()
    private val adapters: Map<String, AdapterRunner> = HashMap()
    val database = Database.connect(Config.getInstance().db.url)

    private val logger: Logger = LoggerFactory.getLogger(Application::class.java)

    public fun getServiceHandlers(): Map<String, ServiceHandler> {
        return serviceHandlers
    }

    fun init() {
        val config = Config.getInstance()
        try {
            val entries: Iterator<*> = config.services.entries.iterator()
            while (entries.hasNext()) {
                val entry = entries.next() as Map.Entry<*, *>
                val name = entry.key as String
                val configService = entry.value as ConfigService
                logger.info("开始初始化适配器 {}", name)
                val clazz = Class.forName(configService.messageHandle)
                val handle = clazz.getConstructor(String::class.java).newInstance(name) as ServiceHandler
                (serviceHandlers as HashMap<String, ServiceHandler>)[name] = handle
            }
            val adapterEntries: Iterator<*> = config.adapters.entries.iterator()
            while (adapterEntries.hasNext()) {
                val entry = adapterEntries.next() as Map.Entry<*, *>
                val name = entry.key as String
                val configAdapter = entry.value as ConfigAdapter
                logger.info("开始初始化服务 {}", name)
                val adapter = ActiveAdapterBase.getInstance(name, configAdapter)
                logger.info("hostname={}, port={}, qmgrName={}", configAdapter.hostName, configAdapter.port,
                        configAdapter.qmgrName)
                println(serviceHandlers)
                println(name)
                adapter.setServiceHandler((serviceHandlers as HashMap<String, ServiceHandler>)[name])
                adapter.run()
                (adapters as HashMap<String, AdapterRunner>)[name] = adapter
            }
        } catch (e: Exception) {
            logger.error("", e)
            throw e
        }
    }
}