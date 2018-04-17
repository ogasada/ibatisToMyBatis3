package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsGreaterEqualTagConverter: ITagConverter {

    /**
     * convert `isGreaterEqual` tag as follows
     *
     * ### before
     *
     * ```
     * <isGreaterEqual open="and (" property="id" compareValue="1" close=")">
     *   id = #id#
     *   <isGreaterEqual prepend="or" property="id2" compareProperty="compareId2">
     *     id = #id2#
     *   </isGreaterEqual>
     * </isGreaterEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="id <![CDATA[>=]]> 1">
     *   and ( id = #id#
     *   <if test="id2 <![CDATA[>=]]> compareId2))">
     *     or id = #id2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isGreaterEqual", "prepend")
            .prependAttributeValueToTextContent("isGreaterEqual", "open")
            .appendAttributeValueToTextContent("isGreaterEqual", "close")
            .createNewAttribute("isGreaterEqual", "test") { node ->
                val attributeValue = node.getAttribute("property")
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue <![CDATA[>=]]> ${node.getAttribute("compareValue")}"
                    node.hasAttribute("compareProperty") -> "$attributeValue <![CDATA[>=]]> ${node.getAttribute("compareProperty")}"
                    else -> ""
                }
            }
            .removeAttribute("isGreaterEqual", "prepend")
            .removeAttribute("isGreaterEqual", "open")
            .removeAttribute("isGreaterEqual", "close")
            .removeAttribute("isGreaterEqual", "property")
            .removeAttribute("isGreaterEqual", "compareValue")
            .removeAttribute("isGreaterEqual", "compareProperty")
            .convertTagName("isGreaterEqual", "if")
}


