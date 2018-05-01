package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

interface ITagConverter {

    /**
     * [xmlDocument] convert to MyBatis Document
     */
    fun convert(xmlDocument: Document): Document

    /**
     * convert filtered [oldTag] to [newTag] in [this] document
     *
     * all oldTag convert when [filter] not defined
     */
    fun Document.convertTagName(oldTag: String, newTag: String, filter: (Element) -> Boolean = { true }): Document {
        val tags = this.getElementsByTagName(oldTag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            if (filter(node)) {
                this.renameNode(node, null, newTag)
            }
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
            if (node.hasAttribute(oldAttribute)) {
                val classValue = node.getAttribute(oldAttribute)
                node.removeAttribute(oldAttribute)
                node.setAttribute(newAttribute, classValue)
            }
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

    /**
     * convert value of [attribute] by [valueConverter] in [tag] of [this] document
     */
    fun Document.convertAttributeValue(tag: String, attribute: String, valueConverter: (String) -> String): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            if (node.hasAttribute(attribute)) {
                node.setAttribute(attribute, valueConverter(node.getAttribute(attribute)))
            }
        }
        return this
    }

    /**
     * create new [attribute] using [valueConverter] in [tag] of [this] document
     */
    fun Document.createNewAttribute(tag: String, attribute: String, valueConverter: (Document, Element) -> String): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            node.setAttribute(attribute, valueConverter(this, node))
        }
        return this
    }

    /**
     * prepend value of [attribute] to text content node in [tag] of [this] document
     */
    fun Document.prependAttributeValueToTextContent(tag: String, attribute: String): Document =
            this.convertAttributeValueToTextContent(tag, attribute) { nodelist, attributeValue ->
                val firstTextContent = nodelist.findFirstTextContent()
                if (firstTextContent != null) {
                    firstTextContent.textContent = firstTextContent.textContent.prepend(attributeValue)
                }
            }

    /**
     * append value of [attribute] to text content node in [tag] of [this] document
     */
    fun Document.appendAttributeValueToTextContent(tag: String, attribute: String): Document =
            this.convertAttributeValueToTextContent(tag, attribute) { nodelist, attributeValue ->
                val lastTextContent = nodelist.findLastTextContent()
                if (lastTextContent != null) {
                    lastTextContent.textContent = lastTextContent.textContent.append(attributeValue)
                }
            }

    /**
     * convert text content using [attributeValueConverter] in [tag] of [this] document
     */
    private fun Document.convertAttributeValueToTextContent(tag: String, attribute: String, attributeValueConverter: (nodelist: NodeList, attributeValue: String) -> (Unit)): Document {
        val tags = this.getElementsByTagName(tag)
        (0 until tags.length).forEach {
            val node = tags.item(it) as Element
            if (node.hasAttribute(attribute)) {
                attributeValueConverter(node.childNodes, node.getAttribute(attribute))
            }
        }
        return this
    }

    /**
     * find first text content in [NodeList]
     *
     * return null when not found text content
     */
    fun NodeList.findFirstTextContent(): Node? {
        (0 until this.length).forEach {
            when (this.item(it).nodeType) {
                Node.TEXT_NODE -> {
                    return this.item(it)
                }
            }
        }
        return null
    }

    /**
     * find last text content in [NodeList]
     *
     * return null when not found text content
     */
    fun NodeList.findLastTextContent(): Node? {
        (0 until this.length).reversed().forEach {
            when (this.item(it).nodeType) {
                Node.TEXT_NODE -> {
                    return this.item(it)
                }
            }
        }
        return null
    }

    /**
     * prepend [prependText]
     *
     * however, append to [prependText] after first blank sequence when [this] string starts with blank
     */
    fun String.prepend(prependText: String): String {
        val pattern = Regex("\\s")
        val blankString = this.takeWhile { pattern.matches(it.toString()) }
        val nonBlankString = this.dropWhile { pattern.matches(it.toString()) }
        return "$blankString $prependText $nonBlankString"
    }

    /**
     * append [appendText]
     *
     * however, append to [prependText] before first blank sequence when [this] string ends with blank
     */
    fun String.append(appendText: String): String {
        val pattern = Regex("\\s")
        val blankString = this.takeLastWhile { pattern.matches(it.toString()) }
        val nonBlankString = this.dropLastWhile { pattern.matches(it.toString()) }
        return "$nonBlankString $appendText $blankString"
    }
}
