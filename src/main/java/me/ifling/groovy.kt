package me.ifling

import groovy.lang.GroovyClassLoader
import groovy.lang.GroovyObject
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*


class GroovyScriptClassCache private constructor() {

    companion object {
        var logger = LoggerFactory.getLogger(Config::class.java)
        private val GROOVY_SCRIPT_CLASS_CACHE: MutableMap<String, Class<*>> = HashMap()
        private val instance = GroovyScriptClassCache()
        fun newInstance(): GroovyScriptClassCache {
            return instance
        }

        fun getClassByKey(key: String): Class<*> {
            return GROOVY_SCRIPT_CLASS_CACHE[key]!!
        }

        fun putClass(key: String, clazz: Class<*>) {
            GROOVY_SCRIPT_CLASS_CACHE[key] = clazz
        }

        fun containsKey(key: String): Boolean {
            return GROOVY_SCRIPT_CLASS_CACHE.containsKey(key)
        }
    }

    fun refresh() {

    }

    fun loadScript(text: String): Class<*>? {
        logger.info("{} {}", "hello", 1)
        val groovyClassLoader = GroovyClassLoader(GroovyClassLoaderEx::class.java.classLoader)
        var groovyClass: Class<*>? = null
        val classKey: String = java.lang.String.valueOf(text.hashCode())
        if (containsKey(classKey)) {
            groovyClass = getClassByKey(classKey)
        } else {
            groovyClass = groovyClassLoader.parseClass(text)
            putClass(classKey, groovyClass!!)
        }
        return groovyClass
    }
}

object GroovyClassLoaderEx {
    @Throws(Exception::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val cache = GroovyScriptClassCache.newInstance()
        val scriptClass = "src/main/java/me/zh/a.groovy"
        val groovyClazz = GroovyScriptClassCache.newInstance()
                .loadScript("src/main/java/me/zh/a.groovy")
        println(groovyClazz)

        val loader = GroovyClassLoader()
        for (i in 0..1) {
//            val clazz: Class<*> = loader.parseClass(File("src/main/java/me/zh/a.groovy"))
//            val go: GroovyObject = clazz.newInstance() as GroovyObject
            val go = groovyClazz!!.getConstructor().newInstance() as GroovyObject
            go.invokeMethod("setName", "abc")
//            clazzObj.invokeMethod("setSex", "Boy")
//            clazzObj.invokeMethod("setAge", "26")
//            System.out.println(clazzObj.invokeMethod("getAllInfo", null))
        }
    }
}