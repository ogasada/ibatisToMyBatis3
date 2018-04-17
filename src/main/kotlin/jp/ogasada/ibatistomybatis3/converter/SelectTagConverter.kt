package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object SelectTagConverter: ITagConverter {

    /**
     * convert `select` tag as follows
     *
     * ### before
     *
     * ```
     * <select id="find" resultClass="String" parameterClass="long">
     * ```
     *
     * ### after
     *
     * ```
     * <select id="find" resultType="String" parameterType="long">
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("select", "parameterClass", "parameterType")
            .convertAttributeName("select", "resultClass", "resultType")
}


