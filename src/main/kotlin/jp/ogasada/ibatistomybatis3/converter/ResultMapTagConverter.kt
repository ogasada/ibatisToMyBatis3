package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object ResultMapTagConverter: ITagConverter {
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("resultMap", "class", "type")
            .removeAttribute("resultMap", "groupBy")
}
