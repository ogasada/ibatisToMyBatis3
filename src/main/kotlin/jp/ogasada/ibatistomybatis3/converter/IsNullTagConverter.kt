package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNullTagConverter: ITagConverter {

    /**
     * convert `isNull` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isNull open="and (" property="name" close=")">
     *   name = #name#
     *   <isNull prepend="or" property="name2">
     *     name = #name2#
     *   </isNull>
     * </isNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name == null">
     *   and ( name = #name#
     *   <if test="name2 == null">
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
     * <isNull open="and (" close=")">
     *   name = #name#
     * </isNull>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="_parameter == null">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNull", "open")
            .prependAttributeValueToTextContent("isNull", "prepend")
            .appendAttributeValueToTextContent("isNull", "close")
            .createNewAttribute("isNull", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                "$attributeValue == null"
            }
            .removeAttribute("isNull", "prepend")
            .removeAttribute("isNull", "open")
            .removeAttribute("isNull", "close")
            .removeAttribute("isNull", "property")
            .convertTagName("isNull", "if")
}


