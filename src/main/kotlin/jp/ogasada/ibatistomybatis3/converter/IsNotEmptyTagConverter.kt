package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsNotEmptyTagConverter: ITagConverter {

    /**
     * convert `isNotEmpty` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isNotEmpty open="and (" property="name" close=")">
     *   name = #name#
     *   <isNotEmpty prepend="or" property="name2">
     *     name = #name2#
     *   </isNotEmpty>
     * </isNotEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals('')))">
     *   and ( name = #name#
     *   <if test="!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or name2 == null or (name2 instanceof java.util.Collection and name2.size() == 0) or (name2.getClass().isArray() and @java.lang.reflect.Array@getLength(name2) == 0) or (name2 instanceof String and name2.equals('')))">
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
     * <isNotEmpty open="and (" close=")">
     *   name = #name#
     * </isNotEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or _parameter == null or (_parameter instanceof java.util.Collection and _parameter.size() == 0) or (_parameter.getClass().isArray() and @java.lang.reflect.Array@getLength(_parameter) == 0) or (_parameter instanceof String and _parameter.equals('')))">
     *   and ( name = #name# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isNotEmpty", "open")
            .prependAttributeValueToTextContent("isNotEmpty", "prepend")
            .appendAttributeValueToTextContent("isNotEmpty", "close")
            .createNewAttribute("isNotEmpty", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                "!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('$attributeValue')) or " +
                        "$attributeValue == null or " +
                        "($attributeValue instanceof java.util.Collection and $attributeValue.size() == 0) or " +
                        "($attributeValue.getClass().isArray() and @java.lang.reflect.Array@getLength($attributeValue) == 0) or " +
                        "($attributeValue instanceof String and $attributeValue.equals('')))"
            }
            .removeAttribute("isNotEmpty", "prepend")
            .removeAttribute("isNotEmpty", "open")
            .removeAttribute("isNotEmpty", "close")
            .removeAttribute("isNotEmpty", "property")
            .convertTagName("isNotEmpty", "if")
}


