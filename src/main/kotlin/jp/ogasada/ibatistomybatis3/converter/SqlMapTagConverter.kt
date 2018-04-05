package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object SqlMapTagConverter: ITagConverter {
    override fun convert(xmlDocument: Document): Document = xmlDocument.convertTagName("sqlMap", "mapper")
}
