package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * load [xml] string and create [Document]
 */
internal fun loadXML(xml: String): Document {
    val inputSource = InputSource()
    inputSource.characterStream = StringReader(xml)
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource)!!
}

/**
 * return true when exists [attributeName] in [tagName] of [xmlDocument]
 */
internal fun existsAttribute(xmlDocument: Document, tagName: String, attributeName: String, index: Int = 0): Boolean {
    val tags = xmlDocument.getElementsByTagName(tagName)
    return (tags.item(index) as Element).hasAttribute(attributeName)
}

/**
 * return [attributeName] value in [tagName] of [xmlDocument]
 */
internal fun attributeValue(xmlDocument: Document, tagName: String, attributeName: String, index: Int = 0): String =
        (xmlDocument.getElementsByTagName(tagName).item(index) as Element).getAttribute(attributeName)

/**
 * return textContent in [tagName] of [xmlDocument]
 */
internal fun textContent(xmlDocument: Document, tagName: String, index: Int = 0): String {
    val nodes = xmlDocument.getElementsByTagName(tagName).item(index).childNodes
    return (0 until nodes.length)
            .filter { nodes.item(it).nodeType == Node.TEXT_NODE }
            .joinToString(separator = "") { nodes.item(it).textContent }
}
