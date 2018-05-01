package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import org.w3c.dom.Element

object ResultTagConverter : ITagConverter {

    /**
     * convert `result` tag as follows
     *
     * ### before
     *
     * ```
     * <resultMap id="result" class="Map">
     *   <result property="id" column="id" javaType="int" />
     *   <result property="detailList" javaType="List" resultMap="jp.ogasada.ibatistomybatis3.detailResult" />
     * </resultMap>
     * <resultMap id="detailResult" class="Map">
     *   <result property="id" column="id" javaType="int" />
     *   <result property="name" column="name" javaType="String" />
     * </resultMap>
     * ```
     *
     * ### after
     *
     * ```
     * <resultMap id="result" class="Map">
     *   <result property="id" column="id" javaType="int" />
     *   <collection property="detailList" javaType="List" resultMap="detailResult" notNullColumn="id, name" />
     * </resultMap>
     * <resultMap id="detailResult" class="Map">
     *   <result property="id" column="id" javaType="int" />
     *   <result property="name" column="name" javaType="String" />
     * </resultMap>
     * ```
     */
    override fun convert(xmlDocument: Document): Document = xmlDocument
            .createNewAttribute("result", "column") { _, node ->
                when {
                    node.hasAttribute("column") -> node.getAttribute("column")
                    node.hasAttribute("property") -> node.getAttribute("property")
                    else -> ""
                }
            }
            .convertAttributeValue("result", "resultMap") { it.takeLastWhile { it != '.' } }
            .convertTagName("result", "collection") { it.hasAttribute("resultMap") }
            .createNewAttribute("collection", "notNullColumn") { document, node ->
                val resultMapNode = document.findResultMapNodeById(node.getAttribute("resultMap"))
                if (resultMapNode != null) {
                    val resultTags = resultMapNode.getElementsByTagName("result")
                    (0 until resultTags.length).fold(listOf<String>()) { acc, element ->
                        acc + (resultTags.item(element) as Element).getAttribute("column")
                    }.joinToString(separator = ", ")
                } else {
                    ""
                }
            }

    private fun Document.findResultMapNodeById(id: String): Element? {
        val resultMapTags = this.getElementsByTagName("resultMap")
        (0 until resultMapTags.length).forEach {
            val node = resultMapTags.item(it) as Element
            if (node.getAttribute("id") == id) {
                return node
            }
        }
        return null
    }
}


