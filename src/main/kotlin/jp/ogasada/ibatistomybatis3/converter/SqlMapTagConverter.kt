package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object SqlMapTagConverter: ITagConverter {

    /**
     * convert `sqlMap` tag as follows
     *
     * before
     *
     * ```
     * <sqlMap namespace="jp.ogasada.test">
     * </sqlMap>
     * ```
     *
     * after
     *
     * ```
     * <mapper namespace="jp.ogasada.test">
     * </mapper>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument.convertTagName("sqlMap", "mapper")
}
