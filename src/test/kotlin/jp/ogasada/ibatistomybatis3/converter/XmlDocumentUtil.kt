package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
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
