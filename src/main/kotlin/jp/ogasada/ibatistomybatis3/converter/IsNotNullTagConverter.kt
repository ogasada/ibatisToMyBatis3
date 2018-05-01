package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotNullTagConverter: ITagConverter {

    /**
     * convert `isNotNull` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isNotNull open="and (" property="name" close=")">
     *   name = #name#
     *   <isNotNull prepend="or" property="name2">
     *     name = #name2#
     *   </isNotNull>
     * </isNotNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name != null">
     *   and ( name = #name#
     *   <if test="name2 != null">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <isNotNull open="and (" close=")">
     *   name = #name#
     * </isNotNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="_parameter != null">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotNull", "open")
            .prependAttributeValueToTextContent("isNotNull", "prepend")
            .appendAttributeValueToTextContent("isNotNull", "close")
            .createNewAttribute("isNotNull", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                "$attributeValue != null"
            }
            .removeAttribute("isNotNull", "prepend")
            .removeAttribute("isNotNull", "open")
            .removeAttribute("isNotNull", "close")
            .removeAttribute("isNotNull", "property")
            .convertTagName("isNotNull", "if")
}


