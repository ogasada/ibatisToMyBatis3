package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object UpdateTagConverter: ITagConverter {

    /**
     * convert `update` tag as follows
     *
     * ### before
     *
     * ```
     * <update id="insert" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     *
     * ### after
     *
     * ```
     * <update id="insert" parameterType="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("update", "parameterClass", "parameterType")
}


