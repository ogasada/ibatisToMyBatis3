package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotNullTagConverter: ITagConverter {

    /**
     * convert `isNotNull` tag as follows
     *
     * ### before
     *
     * ```
     * <isNotNull open="and (" property="name" close=")">
     *   name = #name#
     *   <isNotNull prepend="or" property="name2">
     *     name = #name2#
     *   </isNotNull>
     * </isNotNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name != null">
     *   and ( name = #name#
     *   <if test="name2 != null">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotNull", "prepend")
            .prependAttributeValueToTextContent("isNotNull", "open")
            .appendAttributeValueToTextContent("isNotNull", "close")
            .createNewAttribute("isNotNull", "test") { node ->
                val attributeValue = node.getAttribute("property")
                "$attributeValue != null"
            }
            .removeAttribute("isNotNull", "prepend")
            .removeAttribute("isNotNull", "open")
            .removeAttribute("isNotNull", "close")
            .removeAttribute("isNotNull", "property")
            .convertTagName("isNotNull", "if")
}

