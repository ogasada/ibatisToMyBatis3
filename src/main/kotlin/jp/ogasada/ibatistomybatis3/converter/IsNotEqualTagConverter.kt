package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotEqualTagConverter: ITagConverter {

    /**
     * convert `isNotEqual` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isNotEqual open="and (" property="name" compareValue="foo" close=")">
     *   name = #name#
     *   <isNotEqual prepend="or" property="name2" compareProperty="compareName2">
     *     name = #name2#
     *   </isNotEqual>
     * </isNotEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <!--name != 'foo'--><if test="(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or !name.toString().equals('foo'.toString())">
     *   and ( name = #name#
     *   <!--name2 != compareName2--><if test="(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or !name2.toString().equals(compareName2.toString())">
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
     * <isNotEqual open="and (" compareValue="foo" close=")">
     *   name = #name#
     * </isNotEqual>
     * ```
     *
     * ### after
     *
     * ```
     * <!--_parameter != 'foo'--><if test="(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or !_parameter.toString().equals('foo'.toString())">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotEqual", "open")
            .prependAttributeValueToTextContent("isNotEqual", "prepend")
            .appendAttributeValueToTextContent("isNotEqual", "close")
            .createNewAttribute("isNotEqual", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                when {
                    node.hasAttribute("compareValue") -> {
                        node.parentNode.insertBefore(xmlDocument.createComment("$attributeValue != '${node.getAttribute("compareValue")}'"), node)
                        "(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('$attributeValue')) or " +
                                "!$attributeValue.toString().equals('${node.getAttribute("compareValue")}'.toString())"
                    }
                    node.hasAttribute("compareProperty") -> {
                        node.parentNode.insertBefore(xmlDocument.createComment("$attributeValue != ${node.getAttribute("compareProperty")}"), node)
                        "(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('$attributeValue')) or " +
                                "!$attributeValue.toString().equals(${node.getAttribute("compareProperty")}.toString())"
                    }
                    else -> ""
                }
            }
            .removeAttribute("isNotEqual", "prepend")
            .removeAttribute("isNotEqual", "open")
            .removeAttribute("isNotEqual", "close")
            .removeAttribute("isNotEqual", "property")
            .removeAttribute("isNotEqual", "compareValue")
            .removeAttribute("isNotEqual", "compareProperty")
            .convertTagName("isNotEqual", "if")
}


