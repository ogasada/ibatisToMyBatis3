package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsGreaterThanTagConverter: ITagConverter {

    /**
     * convert `isGreaterThan` tag as follows
     *
     * ## case 1
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
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <isGreaterThan open="and (" compareValue="1" close=")">
     *   id = #id#
     * </isGreaterThan>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="_parameter gt 1">
     *   and ( id = #id# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isGreaterThan", "open")
            .prependAttributeValueToTextContent("isGreaterThan", "prepend")
            .appendAttributeValueToTextContent("isGreaterThan", "close")
            .createNewAttribute("isGreaterThan", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
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


