package me.ifling

import me.liuwj.ktorm.database.Database
import java.lang.reflect.InvocationTargetException

object Initialization {

    @Throws(ClassNotFoundException::class, NoSuchMethodException::class, IllegalAccessException::class, InvocationTargetException::class, InstantiationException::class)
    fun prepareDBGroovy(config: Config, database: Database?) {
        Application.getServiceHandlers().map {
            it.value.prepareDB(database!!)
            it.value.loadGroovy(null)
        }
//        val entries: Iterator<Map.Entry<String, ConfigAdapter>>? = config.adapters?.entries?.iterator()
//        while (entries?.hasNext()!!) {
//            val entry = entries.next() as Map.Entry<*, *>
//            val name = entry.key as String
//            val value = entry.value as ConfigAdapter
//            val service = config.services?.get(name)
//            val clazz = Class.forName(service!!.messageHandle)
//            val handle = clazz.getConstructor(String::class.java).newInstance(name) as ServiceHandler
//            handle.prepareDB(database!!)
//            handle.loadGroovy(null)
//        }
    }

    fun prepareApplication() {
        Application.init()
    }

//    companion object {
//        @Throws(Exception::class)
//        @JvmStatic
//        fun main(args: Array<String>) {
//            val databaseFactory = DatabaseFactory()
//            val init = Initialization()
//            //        init.prepareDB(Config.getInstance(), conn);
//        }
//    }
}