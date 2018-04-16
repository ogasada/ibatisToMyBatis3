package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object DeleteTagConverter: ITagConverter {

    /**
     * convert `delete` tag as follows
     *
     * before
     *
     * ```
     * <delete id="insert" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     *
     * after
     *
     * ```
     * <delete id="insert" parameterType="jp.ogasada.ibatistomybatis3.TestTableEntity" >
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeName("delete", "parameterClass", "parameterType")
}


