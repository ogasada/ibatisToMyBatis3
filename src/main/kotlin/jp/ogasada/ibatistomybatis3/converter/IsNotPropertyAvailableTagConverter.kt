package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotPropertyAvailableTagConverter: ITagConverter {

    /**
     * convert `isNotPropertyAvailable` tag as follows
     *
     * ### before
     *
     * ```
     * <isNotPropertyAvailable open="and (" property="name" close=")">
     *   name = #name3#
     *   <isNotPropertyAvailable prepend="or" property="name2">
     *     name = #name4#
     *   </isNotPropertyAvailable>
     * </isNotPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="!_parameter.containsKey(‘name’)">
     *   and ( name = #name3#
     *   <if test="!_parameter.containsKey(‘name2’)">
     *     or name = #name4#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotPropertyAvailable", "prepend")
            .prependAttributeValueToTextContent("isNotPropertyAvailable", "open")
            .appendAttributeValueToTextContent("isNotPropertyAvailable", "close")
            .createNewAttribute("isNotPropertyAvailable", "test") { node ->
                val attributeValue = node.getAttribute("property")
                "!_parameter.containsKey('$attributeValue')"
            }
            .removeAttribute("isNotPropertyAvailable", "prepend")
            .removeAttribute("isNotPropertyAvailable", "open")
            .removeAttribute("isNotPropertyAvailable", "close")
            .removeAttribute("isNotPropertyAvailable", "property")
            .convertTagName("isNotPropertyAvailable", "if")
}


