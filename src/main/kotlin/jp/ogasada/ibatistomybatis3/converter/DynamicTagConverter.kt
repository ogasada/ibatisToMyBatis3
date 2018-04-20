package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

object DynamicTagConverter: ITagConverter {

    /**
     * convert `dynamic` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <dynamic prepend="where">
     *   <isEqual prepend="and" property="id" compareValue="1">
     *     id = #id#
     *   </isEqual>
     *   <isEqual prepend="and" property="name" compareValue="foo">
     *     name = #name#
     *   </isEqual>
     * </dynamic>
     * ```
     *
     * ### after
     *
     * ```
     * <trim prefix="where" prefixOverrides="AND |OR ">
     *   <isEqual prepend="and" property="id" compareValue="1">
     *     id = #id#
     *   </isEqual>
     *   <isEqual prepend="and" property="name" compareValue="foo">
     *     name = #name#
     *   </isEqual>
     * </trim>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * UPDATE testTable
     * <dynamic prepend="set">
     *   <isNotNull prepend="," property="id">
     *     id = #id#
     *   </isNotNull>
     *   <isNotNull prepend="," property="name">
     *     name = #name#
     *   </isNotNull>
     * </dynamic>
     * WHERE
     *   key = #key#
     * ```
     *
     * ### after
     *
     * ```
     * UPDATE testTable
     * <trim prefix="set" prefixOverrides=", |AND |OR ">
     *   <isNotNull prepend="," property="id">
     *     id = #id#
     *   </isNotNull>
     *   <isNotNull prepend="," property="name">
     *     name = #name#
     *   </isNotNull>
     * </trim>
     * WHERE
     *   key = #key#
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .createNewAttribute("dynamic", "prefix") { node ->
                listOf("prepend", "open").mapNotNull {
                    if (node.hasAttribute(it)) node.getAttribute(it) else null
                }.joinToString(separator = " ")
            }
            .createNewAttribute("dynamic", "prefixOverrides") { node ->
                (listOf("AND ", "OR ") + collectChildPrependValue(node.childNodes))
                        .toSortedSet()
                        .joinToString(separator = "|")
            }
            .convertAttributeName("dynamic", "close", "suffix")
            .removeAttribute("dynamic", "prepend")
            .removeAttribute("dynamic", "open")
            .convertTagName("dynamic", "trim")

    private fun collectChildPrependValue(children: NodeList) = (0 until children.length).mapNotNull {
        val child = children.item(it)
        when (child.nodeType) {
            Node.ELEMENT_NODE -> {
                child as Element
                if (child.hasAttribute("prepend")) "${child.getAttribute("prepend").toUpperCase()} " else null
            }
            else -> null
        }
    }
}


