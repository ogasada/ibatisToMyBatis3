package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotEmptyTagConverter: ITagConverter {

    /**
     * convert `isNotEmpty` tag as follows
     *
     * ### before
     *
     * ```
     * <isNotEmpty open="and (" property="name" close=")">
     *   name = #name#
     *   <isNotEmpty prepend="or" property="name2">
     *     name = #name2#
     *   </isNotEmpty>
     * </isNotEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name != null and name != ''">
     *   and ( name = #name#
     *   <if test="name2 != null and name2 != ''">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotEmpty", "open")
            .prependAttributeValueToTextContent("isNotEmpty", "prepend")
            .appendAttributeValueToTextContent("isNotEmpty", "close")
                val attributeValue = node.getAttribute("property")
                "$attributeValue != null and !$attributeValue.isEmpty()"
            .createNewAttribute("isNotEmpty", "test") { _, node ->
            }
            .removeAttribute("isNotEmpty", "prepend")
            .removeAttribute("isNotEmpty", "open")
            .removeAttribute("isNotEmpty", "close")
            .removeAttribute("isNotEmpty", "property")
            .convertTagName("isNotEmpty", "if")
}


