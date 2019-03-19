package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IterateTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val iterateTagsBeforeConvert = loadedDocument.getElementsByTagName("iterate")
        assertEquals(7, iterateTagsBeforeConvert.length)
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

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 3))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 3))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 3))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 3))
        assertEquals("\n        id = #anyName[]#\n      ", textContent(loadedDocument, "iterate", 3))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 3))

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 4))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 4))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 4))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 4))
        assertEquals("\n        id = #anyName[].name#\n      ", textContent(loadedDocument, "iterate", 4))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 4))

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 5))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 5))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 5))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 5))
        assertEquals("\n        id = \$anyName[]\$\n      ", textContent(loadedDocument, "iterate", 5))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 5))

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 6))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 6))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 6))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 6))
        assertEquals("\n        id = \$anyName[].name\$\n      ", textContent(loadedDocument, "iterate", 6))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 6))

        val convertedDocument = IterateTagConverter.convert(loadedDocument)

        val foreachTagsAfterConvert = convertedDocument.getElementsByTagName("foreach")
        assertEquals(7, foreachTagsAfterConvert.length)
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

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 3))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 3))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 3))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 3))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 3))
        assertEquals("\n        id = #item#\n      ", textContent(convertedDocument, "foreach", 3))

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 4))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 4))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 4))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 4))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 4))
        assertEquals("\n        id = #item.name#\n      ", textContent(convertedDocument, "foreach", 4))

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 5))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 5))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 5))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 5))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 5))
        assertEquals("\n        id = \$item\$\n      ", textContent(convertedDocument, "foreach", 5))

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 6))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 6))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 6))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 6))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 6))
        assertEquals("\n        id = \$item.name\$\n      ", textContent(convertedDocument, "foreach", 6))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 0))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 1))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 2))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 3))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 3))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 3))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 4))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 4))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 4))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 5))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 5))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 5))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 6))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 6))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 6))

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
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = #anyName[]#
                  </iterate>
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = #anyName[].name#
                  </iterate>
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = ${"$"}anyName[]${"$"}
                  </iterate>
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = ${"$"}anyName[].name${"$"}
                  </iterate>
              </select></sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
