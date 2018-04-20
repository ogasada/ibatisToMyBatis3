package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsPropertyAvailableTagConverter: ITagConverter {

    /**
     * convert `isPropertyAvailable` tag as follows
     *
     * ### before
     *
     * ```
     * <isPropertyAvailable open="and (" property="name" close=")">
     *   name = #name#
     *   <isPropertyAvailable prepend="or" property="name2">
     *     name = #name2#
     *   </isPropertyAvailable>
     * </isPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="_parameter.containsKey(‘name’)">
     *   and ( name = #name#
     *   <if test="_parameter.containsKey(‘name2’)">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isPropertyAvailable", "open")
            .prependAttributeValueToTextContent("isPropertyAvailable", "prepend")
            .appendAttributeValueToTextContent("isPropertyAvailable", "close")
            .createNewAttribute("isPropertyAvailable", "test") { node ->
                val attributeValue = node.getAttribute("property")
                "_parameter.containsKey('$attributeValue')"
            }
            .removeAttribute("isPropertyAvailable", "prepend")
            .removeAttribute("isPropertyAvailable", "open")
            .removeAttribute("isPropertyAvailable", "close")
            .removeAttribute("isPropertyAvailable", "property")
            .convertTagName("isPropertyAvailable", "if")
}


