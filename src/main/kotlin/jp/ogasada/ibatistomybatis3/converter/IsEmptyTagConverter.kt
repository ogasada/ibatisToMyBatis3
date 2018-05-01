package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

object IsEmptyTagConverter : ITagConverter {

    /**
     * convert `isEmpty` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <isEmpty property="name" prepend="and" open="(" close=")">
     *   name = #name#
     * </isEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals(''))">
     *   and ( name = #name# )
     * </if>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <isEmpty prepend="and" open="(" close=")">
     *   name = #value#
     * </isEmpty>
     * ```
     *
     * ### after
     *
     * ```
     * <if test="(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or _parameter == null or (_parameter instanceof java.util.Collection and _parameter.size() == 0) or (_parameter.getClass().isArray() and @java.lang.reflect.Array@getLength(_parameter) == 0) or (_parameter instanceof String and _parameter.equals(''))">
     *   and ( name = #value# )
     * </if>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .prependAttributeValueToTextContent("isEmpty", "open")
            .prependAttributeValueToTextContent("isEmpty", "prepend")
            .appendAttributeValueToTextContent("isEmpty", "close")
            .createNewAttribute("isEmpty", "test") { _, node ->
                val attributeValue = if (node.hasAttribute("property")) node.getAttribute("property") else "_parameter"
                "(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('$attributeValue')) or " +
                        "$attributeValue == null or " +
                        "($attributeValue instanceof java.util.Collection and $attributeValue.size() == 0) or " +
                        "($attributeValue.getClass().isArray() and @java.lang.reflect.Array@getLength($attributeValue) == 0) or " +
                        "($attributeValue instanceof String and $attributeValue.equals(''))"
            }
            .removeAttribute("isEmpty", "prepend")
            .removeAttribute("isEmpty", "open")
            .removeAttribute("isEmpty", "close")
            .removeAttribute("isEmpty", "property")
            .convertTagName("isEmpty", "if")
}


