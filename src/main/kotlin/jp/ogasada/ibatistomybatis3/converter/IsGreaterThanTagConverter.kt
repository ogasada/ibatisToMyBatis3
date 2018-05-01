package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsGreaterThanTagConverter: ITagConverter {

    /**
     * convert `isGreaterThan` tag as follows
     *
     * ### before
     *
     * ```
     * <isGreaterThan open="and (" property="id" compareValue="1" close=")">
     *   id = #id#
     *   <isGreaterThan prepend="or" property="id2" compareProperty="compareId2">
     *     id = #id2#
     *   </isGreaterThan>
     * </isGreaterThan>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="id gt 1">
     *   and ( id = #id#
     *   <if test="id2 gt compareId2))">
     *     or id = #id2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isGreaterThan", "open")
            .prependAttributeValueToTextContent("isGreaterThan", "prepend")
            .appendAttributeValueToTextContent("isGreaterThan", "close")
                val attributeValue = node.getAttribute("property")
            .createNewAttribute("isGreaterThan", "test") { _, node ->
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue gt ${node.getAttribute("compareValue")}"
                    node.hasAttribute("compareProperty") -> "$attributeValue gt ${node.getAttribute("compareProperty")}"
                    else -> ""
                }
            }
            .removeAttribute("isGreaterThan", "prepend")
            .removeAttribute("isGreaterThan", "open")
            .removeAttribute("isGreaterThan", "close")
            .removeAttribute("isGreaterThan", "property")
            .removeAttribute("isGreaterThan", "compareValue")
            .removeAttribute("isGreaterThan", "compareProperty")
            .convertTagName("isGreaterThan", "if")
}


