package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotPropertyAvailableTagConverter: ITagConverter {

    /**
     * convert `isNotPropertyAvailable` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isNotPropertyAvailable open="and (" property="name" close=")">
     *   name = #name3#
     *   <isNotPropertyAvailable prepend="or" property="name2">
     *     name = #name4#
     *   </isNotPropertyAvailable>
     * </isNotPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <!--name isNotPropertyAvailable--><if test="!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')">
     *   and ( name = #name3#
     *   <!--name2 isNotPropertyAvailable--><if test="!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')">
     *     or name = #name4#
     *   </if> )
     * </if>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <isNotPropertyAvailable open="and (" close=")">
     *   name = #name3#
     * </isNotPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <!--_parameter isNotPropertyAvailable--><if test="!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')">
     *   and ( name = #name3# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotPropertyAvailable", "open")
            .prependAttributeValueToTextContent("isNotPropertyAvailable", "prepend")
            .appendAttributeValueToTextContent("isNotPropertyAvailable", "close")
            .createNewAttribute("isNotPropertyAvailable", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                node.parentNode.insertBefore(xmlDocument.createComment("$attributeValue isNotPropertyAvailable"), node)
                "!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('$attributeValue')"
            }
            .removeAttribute("isNotPropertyAvailable", "prepend")
            .removeAttribute("isNotPropertyAvailable", "open")
            .removeAttribute("isNotPropertyAvailable", "close")
            .removeAttribute("isNotPropertyAvailable", "property")
            .convertTagName("isNotPropertyAvailable", "if")
}


