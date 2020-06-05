package me.zh.xml

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

internal class Ax : InputStream() {
    @Throws(IOException::class)
    override fun read(): Int {
        return 0
    }
}


class Person(val name: String) {
    init {
        println("aka")
    }

    var children: MutableList<Person> = mutableListOf<Person>()

    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }

    init {
//        println(parent)
    }
}

fun main() {
    val p = Person("a")
    val f = FileInputStream("")
    val bio = ByteArrayOutputStream()

    f.read()
}