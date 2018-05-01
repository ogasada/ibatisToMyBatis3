package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsEqualTagConverter: ITagConverter {

    /**
     * convert `isEqual` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isEqual open="and (" property="name" compareValue="foo" close=")">
     *   name = #name#
     *   <isEqual prepend="or" property="name2" compareProperty="compareName2">
     *     name = #name2#
     *   </isEqual>
     * </isEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('$attributeValue'))) and name.toString().equals('foo'.toString())">
     *   and ( name = #name#
     *   <if test="((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('$attributeValue'))) and name2.toString().equals(compareName2.toString())">
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
     * <isEqual open="and (" compareValue="foo" close=")">
     *   name = #name#
     * </isEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('_parameter'))) and _parameter.toString().equals('foo'.toString())">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isEqual", "open")
            .prependAttributeValueToTextContent("isEqual", "prepend")
            .appendAttributeValueToTextContent("isEqual", "close")
            .createNewAttribute("isEqual", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                when {
                    node.hasAttribute("compareValue") ->
                        "((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('$attributeValue'))) and " +
                                "$attributeValue.toString().equals('${node.getAttribute("compareValue")}'.toString())"
                    node.hasAttribute("compareProperty") ->
                        "((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('$attributeValue'))) and " +
                                "$attributeValue.toString().equals(${node.getAttribute("compareProperty")}.toString())"
                    else -> ""
                }
            }
            .removeAttribute("isEqual", "prepend")
            .removeAttribute("isEqual", "open")
            .removeAttribute("isEqual", "close")
            .removeAttribute("isEqual", "property")
            .removeAttribute("isEqual", "compareValue")
            .removeAttribute("isEqual", "compareProperty")
            .convertTagName("isEqual", "if")
}


