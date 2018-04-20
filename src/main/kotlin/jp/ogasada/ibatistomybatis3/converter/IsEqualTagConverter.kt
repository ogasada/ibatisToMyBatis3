package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsEqualTagConverter: ITagConverter {

    /**
     * convert `isEqual` tag as follows
     *
     * ### before
     *
     * ```
     * <isEqual open="and (" property="name" compareValue="foo" close=")">
     *   name = #name#
     *   <isEqual prepend="or" property="name2" compareProperty="compareName2">
     *     name = #name2#
     *   </isEqual>
     * </isEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name.toString().equals('foo'.toString()))">
     *   and ( name = #name#
     *   <if test="name2.toString().equals(compareName2.toString()))">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isEqual", "open")
            .prependAttributeValueToTextContent("isEqual", "prepend")
            .appendAttributeValueToTextContent("isEqual", "close")
            .createNewAttribute("isEqual", "test") { node ->
                val attributeValue = node.getAttribute("property")
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue.toString().equals('${node.getAttribute("compareValue")}'.toString()))"
                    node.hasAttribute("compareProperty") -> "$attributeValue.toString().equals(${node.getAttribute("compareProperty")}.toString()))"
                    else -> ""
                }
            }
            .removeAttribute("isEqual", "prepend")
            .removeAttribute("isEqual", "open")
            .removeAttribute("isEqual", "close")
            .removeAttribute("isEqual", "property")
            .removeAttribute("isEqual", "compareValue")
            .removeAttribute("isEqual", "compareProperty")
            .convertTagName("isEqual", "if")
}


