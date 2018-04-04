package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object SqlMapFileConverter {
    fun convert(xmlDocument: Document): Document =
            ConverterLoader.load().fold(xmlDocument, { acc, element -> element.convert(acc) })
}
