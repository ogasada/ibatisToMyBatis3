package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
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
internal fun existsAttribute(xmlDocument: Document, tagName: String, attributeName: String): Boolean {
    val tags = xmlDocument.getElementsByTagName(tagName)
    return (0 until tags.length).any {
        (tags.item(it) as Element).hasAttribute(attributeName)
    }
}

internal fun attributeValue(xmlDocument: Document, tagName: String, attributeName: String, index: Int = 0): String =
        (xmlDocument.getElementsByTagName(tagName).item(index) as Element).getAttribute(attributeName)

