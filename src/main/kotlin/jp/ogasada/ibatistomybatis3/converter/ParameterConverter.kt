package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

object ParameterConverter: ITagConverter {

    /**
     * convert parameter as follows
     *
     * ## case 1
     *
     * ### before
     *
     * ```
     * #id#
     * ```
     *
     * ### after
     *
     * ```
     * #{id}
     * ```
     *
     * ## case 2
     *
     * ### before
     *
     * ```
     * $id$
     * ```
     *
     * ### after
     *
     * ```
     * ${id}
     *
     *
     * ## case 3
     *
     * when `parameterType` is primitive or wrapper class in `select`, `insert`, `update` and `delete` tag
     *
     * ### before
     *
     * ```
     * <select id="find" parameterType="String" resultType="Map">
     *     ・・・
     *     #value#
     *     $value$
     *     ・・・
     * </select>
     * ```
     *
     * ### after
     *
     * ```
     * <select id="find" parameterType="String" resultType="Map">
     *     ・・・
     *     #{_parameter}
     *     ${_parameter}
     *     ・・・
     * </select>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .convertParameter()
            .convertPrimitiveParameter("select")
            .convertPrimitiveParameter("insert")
            .convertPrimitiveParameter("update")
            .convertPrimitiveParameter("delete")

    private fun Document.convertParameter(): Document {
        val mapperTags = this.getElementsByTagName("mapper")
        (0 until mapperTags.length).forEach {
            convertParameter(mapperTags.item(it) as Element) { it.replace("#([^# ]+?)#".toRegex(), "#{$1}").replace("\\$([^$ ]+?)\\$".toRegex(), "\\\${$1}") }
        }
        return this
    }

    private fun Document.convertPrimitiveParameter(tag: String): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            val parameterType = node.getAttribute("parameterType").toUpperCase()
            when (parameterType) {
                "STRING", "INT", "INTEGER", "LONG", "DOUBLE", "FLOAT", "BOOLEAN", "CHAR", "CHARACTER", "SHORT", "BYTE" -> convertParameter(node) { it.replace("\\$\\{[^}]*}".toRegex(), "\\\${value}") }
            }
        }
        return this
    }

    private fun convertParameter(node: Node, converter: (String) -> String) {
        when (node.nodeType) {
            Node.ELEMENT_NODE -> {
                node as Element
                if (node.hasChildNodes()) {
                    val childNodes = node.childNodes
                    (0 until childNodes.length).forEach {
                        convertParameter(childNodes.item(it), converter)
                    }
                }
            }
            Node.TEXT_NODE -> {
                node.textContent = converter(node.textContent)
            }
        }
    }
}


