package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document

interface ITagConverter {

    /**
     * [xmlDocument] convert to MyBatis Document
     */
    fun convert(xmlDocument: Document): Document

    /**
     * convert [oldTag] to [newTag] in [xmlDocument]
     */
    fun Document.convertTagName(oldTag: String, newTag: String): Document {
        val tags = this.getElementsByTagName(oldTag)
        (0 until tags.length).forEach {
            val node = tags.item(it)
            this.renameNode(node, null, newTag)
        }
        return this
    }
    }
}
