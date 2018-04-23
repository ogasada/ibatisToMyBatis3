package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsLessEqualTagConverter: ITagConverter {

    /**
     * convert `isLessEqual` tag as follows
     *
     * ### before
     *
     * ```
     * <isLessEqual open="and (" property="id" compareValue="1" close=")">
     *   id = #id#
     *   <isLessEqual prepend="or" property="id2" compareProperty="compareId2">
     *     id = #id2#
     *   </isLessEqual>
     * </isLessEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="id lte 1">
     *   and ( id = #id#
     *   <if test="id2 lte compareId2))">
     *     or id = #id2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isLessEqual", "open")
            .prependAttributeValueToTextContent("isLessEqual", "prepend")
            .appendAttributeValueToTextContent("isLessEqual", "close")
            .createNewAttribute("isLessEqual", "test") { node ->
                val attributeValue = node.getAttribute("property")
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue lte ${node.getAttribute("compareValue")}"
                    node.hasAttribute("compareProperty") -> "$attributeValue lte ${node.getAttribute("compareProperty")}"
                    else -> ""
                }
            }
            .removeAttribute("isLessEqual", "prepend")
            .removeAttribute("isLessEqual", "open")
            .removeAttribute("isLessEqual", "close")
            .removeAttribute("isLessEqual", "property")
            .removeAttribute("isLessEqual", "compareValue")
            .removeAttribute("isLessEqual", "compareProperty")
            .convertTagName("isLessEqual", "if")
}


