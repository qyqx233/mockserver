package me.zh

import me.ifling.service.IdMapDao
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.select

fun main() {
    val database = Database.connect("jdbc:sqlite:identify.sqlite3")
    database.useConnection {  }

//    database.useTransaction {
    val n = database.insert(IdMapDao) {
        it.name to "jerry"
        it.id to "313121123"
        it.photo to "adsfjpqwejrpj"
    }
    println(n)

    for (row in database.from(IdMapDao).select()) {
        println(row[IdMapDao.id])
    }

//        for (row in database.from(Employees).select()) {
//            println(row[Employees.name])
//        }
}