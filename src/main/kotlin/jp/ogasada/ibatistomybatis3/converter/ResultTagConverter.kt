package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object ResultTagConverter: ITagConverter {

    /**
     * convert `result` tag as follows
     *
     * before
     *
     * ```
     * <result property="id" column="id" javaType="int" />
     * <result property="detailList" javaType="List" resultMap="jp.ogasada.ibatistomybatis3.detailResult" />
     * ```
     *
     * after
     *
     * ```
     * <result property="id" column="id" javaType="int" />
     * <result property="detailList" javaType="List" resultMap="detailResult" />
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertAttributeValue("result", "resultMap") { it.takeLastWhile { it != '.' } }
}


