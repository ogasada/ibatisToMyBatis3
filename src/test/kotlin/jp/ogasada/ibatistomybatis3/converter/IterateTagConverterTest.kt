package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IterateTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val iterateTagsBeforeConvert = loadedDocument.getElementsByTagName("iterate")
        assertEquals(3, iterateTagsBeforeConvert.length)
        assertEquals("list", attributeValue(loadedDocument, "iterate", "property", 0))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 0))
        assertEquals(" OR ", attributeValue(loadedDocument, "iterate", "conjunction", 0))
        assertEquals("\n        (name = #list[].name#\n        \n        )\n      ", textContent(loadedDocument, "iterate", 0))

        assertEquals("list[].subList", attributeValue(loadedDocument, "iterate", "property", 1))
        assertEquals("id IN", attributeValue(loadedDocument, "iterate", "prepend", 1))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 1))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 1))
        assertEquals(",", attributeValue(loadedDocument, "iterate", "conjunction", 1))
        assertEquals("\n          #list[].subList[].id#\n        ", textContent(loadedDocument, "iterate", 1))

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 2))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 2))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 2))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 2))
        assertEquals("\n        id = #[]#\n      ", textContent(loadedDocument, "iterate", 2))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 2))

        val convertedDocument = IterateTagConverter.convert(loadedDocument)

        val foreachTagsAfterConvert = convertedDocument.getElementsByTagName("foreach")
        assertEquals(3, foreachTagsAfterConvert.length)
        assertEquals("listItem", attributeValue(convertedDocument, "foreach", "item", 0))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 0))
        assertEquals("(", attributeValue(convertedDocument, "foreach", "open", 0))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 0))
        assertEquals(" OR ", attributeValue(convertedDocument, "foreach", "separator", 0))
        assertEquals("\n        (name = #listItem.name#\n        \n        )\n      ", textContent(convertedDocument, "foreach", 0))

        assertEquals("listItem.subListItem", attributeValue(convertedDocument, "foreach", "item", 1))
        assertEquals("listItem.subList", attributeValue(convertedDocument, "foreach", "collection", 1))
        assertEquals("id IN (", attributeValue(convertedDocument, "foreach", "open", 1))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 1))
        assertEquals(",", attributeValue(convertedDocument, "foreach", "separator", 1))
        assertEquals("\n          #listItem.subListItem.id#\n        ", textContent(convertedDocument, "foreach", 1))

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 2))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 2))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 2))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 2))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 2))
        assertEquals("\n        id = #item#\n      ", textContent(convertedDocument, "foreach", 2))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 0))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 1))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 2))

    }

    private fun loadValidDocument(): Document {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE sqlMap
              PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
              "http://ibatis.apache.org/dtd/sql-map-2.dtd">

            <sqlMap namespace="jp.ogasada.test">
              <resultMap id="findResult" class="HashMap" groupBy="id">
                <result property="id" column="id" javaType="int" />
                <result property="name" column="name" javaType="String" />
                <result property="detailList" javaType="List" resultMap="jp.ogasada.ibatistomybatis3.detailResult" />
              </resultMap>
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                  <iterate property="list" open="(" close=")" conjunction=" OR " >
                    (name = #list[].name#
                    <iterate property="list[].subList" prepend="id IN" open="(" close=")" conjunction="," >
                      #list[].subList[].id#
                    </iterate>
                    )
                  </iterate>
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = #[]#
                  </iterate>
              </select></sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
