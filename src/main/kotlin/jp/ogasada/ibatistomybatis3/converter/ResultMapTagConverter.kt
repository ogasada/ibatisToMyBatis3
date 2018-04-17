package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object ResultMapTagConverter: ITagConverter {

    /**
     * convert `resultMap` tag as follows
     *
     * ### before
     *
     * ```
     * <resultMap id="findResult" class="HashMap" groupBy="id">
     * </resultMap>
     * ```
     *
     * ### after
     *
     * ```
     * <resultMap id="findResult" type="HashMap">
     * </resultMap>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("resultMap", "class", "type")
            .removeAttribute("resultMap", "groupBy")
}
