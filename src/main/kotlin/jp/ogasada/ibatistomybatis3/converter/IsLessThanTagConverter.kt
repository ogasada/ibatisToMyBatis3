package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsLessThanTagConverter: ITagConverter {

    /**
     * convert `isLessThan` tag as follows
     *
     * ### before
     *
     * ```
     * <isLessThan open="and (" property="id" compareValue="1" close=")">
     *   id = #id#
     *   <isLessThan prepend="or" property="id2" compareProperty="compareId2">
     *     id = #id2#
     *   </isLessThan>
     * </isLessThan>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="id lt 1">
     *   and ( id = #id#
     *   <if test="id2 lt compareId2))">
     *     or id = #id2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isLessThan", "open")
            .prependAttributeValueToTextContent("isLessThan", "prepend")
            .appendAttributeValueToTextContent("isLessThan", "close")
            .createNewAttribute("isLessThan", "test") { node ->
                val attributeValue = node.getAttribute("property")
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue lt ${node.getAttribute("compareValue")}"
                    node.hasAttribute("compareProperty") -> "$attributeValue lt ${node.getAttribute("compareProperty")}"
                    else -> ""
                }
            }
            .removeAttribute("isLessThan", "prepend")
            .removeAttribute("isLessThan", "open")
            .removeAttribute("isLessThan", "close")
            .removeAttribute("isLessThan", "property")
            .removeAttribute("isLessThan", "compareValue")
            .removeAttribute("isLessThan", "compareProperty")
            .convertTagName("isLessThan", "if")
}


