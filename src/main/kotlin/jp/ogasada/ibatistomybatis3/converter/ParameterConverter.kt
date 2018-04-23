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
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument.convertParameter()

    private fun Document.convertParameter(): Document {
        fun loop(node: Node) {
            when (node.nodeType) {
                Node.ELEMENT_NODE -> {
                    node as Element
                    if (node.hasChildNodes()) {
                        val childNodes = node.childNodes
                        (0 until childNodes.length).forEach {
                            loop(childNodes.item(it))
                        }
                    }
                }
                Node.TEXT_NODE -> {
                    val textContent = node.textContent
                    node.textContent = textContent.replace("#([^# ]+?)#".toRegex(), "#{$1}").replace("\\$([^$ ]+?)\\$".toRegex(), "\\\${$1}")
                }
            }
        }

        val mapperTags = this.getElementsByTagName("mapper")
        (0 until mapperTags.length).forEach { loop(mapperTags.item(it) as Element) }
        return this
    }
}


