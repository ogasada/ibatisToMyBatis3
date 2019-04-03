package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsPropertyAvailableTagConverter : ITagConverter {

    /**
     * convert `isPropertyAvailable` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isPropertyAvailable open="and (" property="name" close=")">
     *   name = #name#
     *   <isPropertyAvailable prepend="or" property="name2">
     *     name = #name2#
     *   </isPropertyAvailable>
     * </isPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <!--name isPropertyAvailable--><if test="(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name'))">
     *   and ( name = #name#
     *   <!--name2 isPropertyAvailable--><if test="(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name2'))">
     *     or name = #name2#
     *   </if> )
     * </if>
     * ```
     *
     * * ## case 2
     *
     * ### before
     *
     * ```
     * <isPropertyAvailable open="and (" close=")">
     *   name = #name#
     * </isPropertyAvailable>
     * ```
     *
     * ### after
     *
     * ```
     * <!--_parameter isPropertyAvailable--><if test="(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('_parameter'))">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isPropertyAvailable", "open")
            .prependAttributeValueToTextContent("isPropertyAvailable", "prepend")
            .appendAttributeValueToTextContent("isPropertyAvailable", "close")
            .createNewAttribute("isPropertyAvailable", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                node.parentNode.insertBefore(xmlDocument.createComment("$attributeValue isPropertyAvailable"), node)
                "(!_parameter instanceof java.util.Map) or " +
                        "(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('$attributeValue'))"
            }
            .removeAttribute("isPropertyAvailable", "prepend")
            .removeAttribute("isPropertyAvailable", "open")
            .removeAttribute("isPropertyAvailable", "close")
            .removeAttribute("isPropertyAvailable", "property")
            .convertTagName("isPropertyAvailable", "if")
}


