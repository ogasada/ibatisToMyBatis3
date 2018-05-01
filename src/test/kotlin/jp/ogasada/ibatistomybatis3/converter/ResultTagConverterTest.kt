package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class ResultTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val resultTagsBeforeConvert = loadedDocument.getElementsByTagName("result")
        assertEquals(5, resultTagsBeforeConvert.length)
        assertEquals("id", attributeValue(loadedDocument, "result", "property", 0))
        assertEquals("name", attributeValue(loadedDocument, "result", "property", 1))
        assertEquals("detailList", attributeValue(loadedDocument, "result", "property", 2))
        assertEquals("id", attributeValue(loadedDocument, "result", "property", 3))
        assertEquals("detailName", attributeValue(loadedDocument, "result", "property", 4))
        assertEquals("id", attributeValue(loadedDocument, "result", "column", 0))
        assertFalse(existsAttribute(loadedDocument, "result", "column", 1))
        assertEquals("id", attributeValue(loadedDocument, "result", "column", 3))
        assertEquals("detailName", attributeValue(loadedDocument, "result", "column", 4))
        assertEquals("int", attributeValue(loadedDocument, "result", "javaType", 0))
        assertEquals("String", attributeValue(loadedDocument, "result", "javaType", 1))
        assertEquals("List", attributeValue(loadedDocument, "result", "javaType", 2))
        assertEquals("int", attributeValue(loadedDocument, "result", "javaType", 3))
        assertEquals("String", attributeValue(loadedDocument, "result", "javaType", 4))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 0))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 1))
        assertTrue(existsAttribute(loadedDocument, "result", "resultMap", 2))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 3))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 4))
        assertEquals("jp.ogasada.ibatistomybatis3.detailResult", attributeValue(loadedDocument, "result", "resultMap", 2))

        val convertedDocument = ResultTagConverter.convert(loadedDocument)

        val resultTagsAfterConvert = convertedDocument.getElementsByTagName("result")
        assertEquals(4, resultTagsAfterConvert.length)
        assertEquals("id", attributeValue(convertedDocument, "result", "property", 0))
        assertEquals("name", attributeValue(convertedDocument, "result", "property", 1))
        assertEquals("id", attributeValue(convertedDocument, "result", "property", 2))
        assertEquals("detailName", attributeValue(convertedDocument, "result", "property", 3))
        assertEquals("id", attributeValue(convertedDocument, "result", "column", 0))
        assertEquals("name", attributeValue(convertedDocument, "result", "column", 1))
        assertEquals("id", attributeValue(convertedDocument, "result", "column", 2))
        assertEquals("detailName", attributeValue(convertedDocument, "result", "column", 3))
        assertEquals("int", attributeValue(convertedDocument, "result", "javaType", 0))
        assertEquals("String", attributeValue(convertedDocument, "result", "javaType", 1))
        assertEquals("int", attributeValue(convertedDocument, "result", "javaType", 2))
        assertEquals("String", attributeValue(convertedDocument, "result", "javaType", 3))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 0))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 1))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 2))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 3))
        val collectionTagsAfterConvert = convertedDocument.getElementsByTagName("collection")
        assertEquals(1, collectionTagsAfterConvert.length)
        assertEquals("List", attributeValue(convertedDocument, "collection", "javaType", 0))
        assertEquals("detailList", attributeValue(convertedDocument, "collection", "property", 0))
        assertEquals("detailResult", attributeValue(convertedDocument, "collection", "resultMap", 0))
        assertEquals("id, detailName", attributeValue(convertedDocument, "collection", "notNullColumn", 0))
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
                <result property="name" javaType="String" />
                <result property="detailList" javaType="List" resultMap="jp.ogasada.ibatistomybatis3.detailResult" />
              </resultMap>
              <resultMap id="detailResult" class="HashMap">
                <result property="id" column="id" javaType="int" />
                <result property="detailName" column="detailName" javaType="String" />
              </resultMap>
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  a.id,
                  a.name,
                  b.id,
                  b.detailName
                FROM
                  testTable a inner join detailTestTable b on b.id = a.detailTestTable_id
                WHERE
                  a.id = #id#
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
