package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object SqlMapConverter: ITagConverter {
    override fun convert(xmlDocument: Document): Document = convertTagName(xmlDocument, "sqlMap", "mapper")
}
