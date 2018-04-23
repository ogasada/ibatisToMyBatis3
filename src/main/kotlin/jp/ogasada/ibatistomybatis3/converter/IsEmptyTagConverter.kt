package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsEmptyTagConverter: ITagConverter {

    /**
     * convert `isEmpty` tag as follows
     *
     * ### before
     *
     * ```
     * <isEmpty open="and (" property="name" close=")">
     *   name = #name#
     *   <isEmpty prepend="or" property="name2">
     *     name = #name2#
     *   </isEmpty>
     * </isEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="name == null or name == ''">
     *   and ( name = #name#
     *   <if test="name2 == null or name2 == ''">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isEmpty", "open")
            .prependAttributeValueToTextContent("isEmpty", "prepend")
            .appendAttributeValueToTextContent("isEmpty", "close")
            .createNewAttribute("isEmpty", "test") { node ->
                val attributeValue = node.getAttribute("property")
                "$attributeValue == null or $attributeValue.isEmpty()"
            }
            .removeAttribute("isEmpty", "prepend")
            .removeAttribute("isEmpty", "open")
            .removeAttribute("isEmpty", "close")
            .removeAttribute("isEmpty", "property")
            .convertTagName("isEmpty", "if")
}


