package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNullTagConverter: ITagConverter {

    /**
     * convert `isNull` tag as follows
     *
     * ### before
     *
     * ```
     * <isNull open="and (" property="name" close=")">
     *   name = #name#
     *   <isNull prepend="or" property="name2">
     *     name = #name2#
     *   </isNull>
     * </isNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name == null">
     *   and ( name = #name#
     *   <if test="name2 == null">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNull", "prepend")
            .prependAttributeValueToTextContent("isNull", "open")
            .appendAttributeValueToTextContent("isNull", "close")
            .createNewAttribute("isNull", "test") { node ->
                val attributeValue = node.getAttribute("property")
                "$attributeValue == null"
            }
            .removeAttribute("isNull", "prepend")
            .removeAttribute("isNull", "open")
            .removeAttribute("isNull", "close")
            .removeAttribute("isNull", "property")
            .convertTagName("isNull", "if")
}


