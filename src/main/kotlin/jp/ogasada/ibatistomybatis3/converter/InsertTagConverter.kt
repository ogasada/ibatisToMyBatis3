package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object InsertTagConverter: ITagConverter {

    /**
     * convert `insert` tag as follows
     *
     * before
     *
     * ```
     * <insert id="insert" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     *
     * after
     *
     * ```
     * <insert id="insert" parameterType="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("insert", "parameterClass", "parameterType")
}


