package me.zh.xml

import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.Node
import java.io.File


class XMLParser internal constructor(xml: String) {
    private var document: Document = DocumentHelper.parseText(xml)
    private var root: Element = document.rootElement


    fun selectSingleNode(path: String): Node? {
        return document.selectSingleNode(path)
    }
}

fun main() {
    val p = XMLParser(File("./src/main/java/me/zh/xml/bookstore.xml").readText())
}