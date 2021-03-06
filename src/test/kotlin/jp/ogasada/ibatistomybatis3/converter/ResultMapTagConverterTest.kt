package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.w3c.dom.Element

internal class ResultMapTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val resultMapTagsBeforeConvert = loadedDocument.getElementsByTagName("resultMap")
        assertEquals(1, resultMapTagsBeforeConvert.length)
        assertTrue(existsAttribute(loadedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "id"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "groupBy"))
        assertFalse(existsAttribute(loadedDocument, "resultMap", "type"))
        assertEquals("findResult", attributeValue(loadedDocument, "resultMap", "id"))
        assertEquals("HashMap", attributeValue(loadedDocument, "resultMap", "class"))
        assertEquals("id", attributeValue(loadedDocument, "resultMap", "groupBy"))

        val convertedDocument = ResultMapTagConverter.convert(loadedDocument)

        val resultMapTagsAfterConvert = convertedDocument.getElementsByTagName("resultMap")
        assertEquals(1, resultMapTagsAfterConvert.length)
        assertFalse(existsAttribute(loadedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "id"))
        assertFalse(existsAttribute(loadedDocument, "resultMap", "groupBy"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "type"))
        assertEquals("findResult", attributeValue(loadedDocument, "resultMap", "id"))
        assertEquals("HashMap", attributeValue(loadedDocument, "resultMap", "type"))
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
              </resultMap>
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = #id#
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
