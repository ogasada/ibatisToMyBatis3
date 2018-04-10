package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element

interface ITagConverter {

    /**
     * [xmlDocument] convert to MyBatis Document
     */
    fun convert(xmlDocument: Document): Document

    /**
     * convert [oldTag] to [newTag] in [this] document
     */
    fun Document.convertTagName(oldTag: String, newTag: String): Document {
        val tags = this.getElementsByTagName(oldTag)
        (0 until tags.length).forEach {
            val node = tags.item(it)
            this.renameNode(node, null, newTag)
        }
        return this
    }

    /**
     * convert [oldAttribute] to [newAttribute] in [tag] of [this] document
     */
    fun Document.convertAttributeName(tag: String, oldAttribute: String, newAttribute: String): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            val classValue = node.getAttribute(oldAttribute)
            node.removeAttribute(oldAttribute)
            node.setAttribute(newAttribute, classValue)
        }
        return this
    }

    /**
     * remove [attribute] in [tag] of [this] document
     */
    fun Document.removeAttribute(tag: String, attribute: String): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            node.removeAttribute(attribute)
        }
        return this
    }
}
