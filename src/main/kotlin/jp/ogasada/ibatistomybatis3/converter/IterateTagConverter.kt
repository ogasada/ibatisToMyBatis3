package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

object IterateTagConverter: ITagConverter {

    /**
     * convert `iterate` tag as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * <iterate property="list" open="(" close=") " conjunction=" OR " >
     *   (name = #list[].name#
     *   <iterate property="list[].subList" prepend="id IN" open="(" close=") " conjunction="," >
     *     #list[].subList[].id#
     *   </iterate>
     *   )
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="listItem" collection="list" open="(" close=") " separator=" OR " >
     *   (name = #listItem.name#
     *   <foreach item="listItem.subListItem" collection="listItem.subList" open="id IN (" close=") " separator="," >
     *     #listItem.subListItem.id#
     *   </foreach>
     *   )
     * </foreach>
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * <iterate open="(" close=") " conjunction=" OR " >
     *   (name = #[].name#)
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="item" collection="list" open="(" close=") " separator=" OR " >
     *   (name = #item.name#)
     * </foreach>
     * ```
     *
     * ## case 3
     *
     * ### before
     *
     * ```
     * <iterate open="(" close=") " conjunction=" OR " >
     *   (name = #anyName[]#)
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="item" collection="list" open="(" close=") " separator=" OR " >
     *   (name = #item#)
     * </foreach>
     * ```
     *
     * ## case 4
     *
     * ### before
     *
     * ```
     * <iterate open="(" close=") " conjunction=" OR " >
     *   (name = #anyName[].name#)
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="item" collection="list" open="(" close=") " separator=" OR " >
     *   (name = #item.name#)
     * </foreach>
     * ```
     *
     * ## case 5
     *
     * ### before
     *
     * ```
     * <iterate open="(" close=") " conjunction=" OR " >
     *   (name = $anyName[]$)
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="item" collection="list" open="(" close=") " separator=" OR " >
     *   (name = $item$)
     * </foreach>
     * ```
     *
     * ## case 6
     *
     * ### before
     *
     * ```
     * <iterate open="(" close=") " conjunction=" OR " >
     *   (name = $anyName[].name$)
     * </iterate>
     * ```
     *
     * ### after
     *
     * ```
     * <foreach item="item" collection="list" open="(" close=") " separator=" OR " >
     *   (name = $item.name$)
     * </foreach>
     * ```
     *
     */
    override fun convert(xmlDocument: Document): Document = convertAccessorValue(xmlDocument)
            .createNewAttribute("iterate", "item") { _, node ->
                if (node.hasAttribute("property")) "${node.getAttribute("property")}Item" else "item"
            }
            .createNewAttribute("iterate", "open") { _, node ->
                listOf("prepend", "open").mapNotNull {
                    if (node.hasAttribute(it)) node.getAttribute(it) else null
                }.joinToString(separator = " ")
            }
            .createNewAttribute("iterate", "collection") { _, node ->
                if (node.hasAttribute("property")) node.getAttribute("property") else "list"
            }
            .convertAttributeName("iterate", "conjunction", "separator")
            .removeAttribute("iterate", "prepend")
            .removeAttribute("iterate", "property")
            .convertTagName("iterate", "foreach")

    private fun convertAccessorValue(xmlDocument: Document): Document {
        fun loop(node: Node, oldAccessor: String, newAccessor: String, isRegexp: Boolean) {
            when (node.nodeType) {
                Node.ELEMENT_NODE -> {
                    node as Element
                    if (node.hasChildNodes()) {
                        val childNodes = node.childNodes
                        (0 until childNodes.length).forEach {
                            loop(childNodes.item(it), oldAccessor, newAccessor, isRegexp)
                        }
                    }
                    val propertyValue = node.getAttribute("property")
                    if (propertyValue.contains(oldAccessor)) {
                        node.setAttribute("property", propertyValue.replace(oldAccessor, newAccessor))
                    }
                }
                Node.TEXT_NODE -> {
                    val textContent = node.textContent
                    if (textContent.contains(oldAccessor)) {
                        node.textContent = if (isRegexp) textContent.replace("""([#|$]).*${Regex.escape(oldAccessor)}(.*[#|$])""".toRegex(), "$1$newAccessor$2") else textContent.replace(oldAccessor, newAccessor)
                    }
                }
            }
        }

        val iterateTags = xmlDocument.getElementsByTagName("iterate")
        (0 until iterateTags.length).reversed().forEach {
            val node = iterateTags.item(it) as Element
            val propertyValue = node.getAttribute("property")
            val (oldAccessor, newAccessor, isRegexp) = if (node.hasAttribute("property")) Triple("${propertyValue}[]", "${propertyValue}Item", false) else Triple("[]", "item", true)
            loop(node, oldAccessor, newAccessor, isRegexp)
        }
        return xmlDocument
    }
}


