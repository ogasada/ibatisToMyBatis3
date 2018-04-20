package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotEqualTagConverter: ITagConverter {

    /**
     * convert `isNotEqual` tag as follows
     *
     * ### before
     *
     * ```
     * <isNotEqual open="and (" property="name" compareValue="foo" close=")">
     *   name = #name#
     *   <isNotEqual prepend="or" property="name2" compareProperty="compareName2">
     *     name = #name2#
     *   </isNotEqual>
     * </isNotEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="!name.toString().equals('foo'.toString()))">
     *   and ( name = #name#
     *   <if test="!name2.toString().equals(compareName2.toString()))">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotEqual", "open")
            .prependAttributeValueToTextContent("isNotEqual", "prepend")
            .appendAttributeValueToTextContent("isNotEqual", "close")
            .createNewAttribute("isNotEqual", "test") { node ->
                val attributeValue = node.getAttribute("property")
                when {
                    node.hasAttribute("compareValue") -> "!$attributeValue.toString().equals('${node.getAttribute("compareValue")}'.toString()))"
                    node.hasAttribute("compareProperty") -> "!$attributeValue.toString().equals(${node.getAttribute("compareProperty")}.toString()))"
                    else -> ""
                }
            }
            .removeAttribute("isNotEqual", "prepend")
            .removeAttribute("isNotEqual", "open")
            .removeAttribute("isNotEqual", "close")
            .removeAttribute("isNotEqual", "property")
            .removeAttribute("isNotEqual", "compareValue")
            .removeAttribute("isNotEqual", "compareProperty")
            .convertTagName("isNotEqual", "if")
}


