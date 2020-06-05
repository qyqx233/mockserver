package me.ifling.service

import me.ifling.Application
import me.ifling.Initialization
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.entity.Tuple2
import me.liuwj.ktorm.schema.Column
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

data class IdentifyReqBean(val MsgNo: String, val MsgID: String)

object IdMapDao : Table<Nothing>("identify_map") {
    val id by varchar("id").primaryKey()
    val name by varchar("name")
    val photo by varchar("photo")
}

object GroovyScriptDao : Table<Nothing>("groovy_script") {
    val id by int("id").primaryKey()
    val name by varchar("name")
    val category by varchar("category")
    val content by varchar("content")
    val timestamp by int("timestamp")
}

fun main() {
    println(IdMapDao.name)
    var o: Column<String> = IdMapDao.name
    Initialization.prepareApplication()
    for (row in Application.database.from(IdMapDao).select()) {
        println(row[IdMapDao.id])
    }
    val query = Application.database.from(IdMapDao).select()
    val arr = ArrayList<Tuple2<String, String>>()
    for (row in query) {
        arr.add(Tuple2(row[IdMapDao.name], row[IdMapDao.id]) as Tuple2<String, String>)
    }
//    val arr = ArrayList<Tuple2<String, String>>(query.totalRecords)
//     query.rowSet.getString(1)
//    val employees = Application.database.sequenceOf(IdMapDao).toCollection(ArrayList())
//    println(employees)
//    println(list[0][IdMapDao.id])
//    println(list[0].getObject(1, String::class.java))
}