package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsGreaterEqualTagConverter: ITagConverter {

    /**
     * convert `isGreaterEqual` tag as follows
     *
     * ## case 1
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
     * <if test="id gte 1">
     *   and ( id = #id#
     *   <if test="id2 gte compareId2))">
     *     or id = #id2#
     *   </if> )
     * </if>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <isGreaterEqual open="and (" compareValue="1" close=")">
     *   id = #id#
     * </isGreaterEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="_parameter gte 1">
     *   and ( id = #id# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isGreaterEqual", "open")
            .prependAttributeValueToTextContent("isGreaterEqual", "prepend")
            .appendAttributeValueToTextContent("isGreaterEqual", "close")
            .createNewAttribute("isGreaterEqual", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                when {
                    node.hasAttribute("compareValue") -> "$attributeValue gte ${node.getAttribute("compareValue")}"
                    node.hasAttribute("compareProperty") -> "$attributeValue gte ${node.getAttribute("compareProperty")}"
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


