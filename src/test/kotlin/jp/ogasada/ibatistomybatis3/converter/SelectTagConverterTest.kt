package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class SelectTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val selectTagsBeforeConvert = loadedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsBeforeConvert.length)
        assertEquals("find", attributeValue(loadedDocument, "select", "id", 0))
        assertEquals("findResult", attributeValue(loadedDocument, "select", "resultMap", 0))
        assertEquals("long", attributeValue(loadedDocument, "select", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 0))
        assertEquals("find2", attributeValue(loadedDocument, "select", "id", 1))
        assertEquals("String", attributeValue(loadedDocument, "select", "resultClass", 1))
        assertEquals("long", attributeValue(loadedDocument, "select", "parameterClass", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 1))

        val convertedDocument = SelectTagConverter.convert(loadedDocument)

        val selectTagsAfterConvert = convertedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsAfterConvert.length)
        assertEquals("find", attributeValue(convertedDocument, "select", "id", 0))
        assertEquals("findResult", attributeValue(convertedDocument, "select", "resultMap", 0))
        assertEquals("long", attributeValue(convertedDocument, "select", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 0))
        assertEquals("find2", attributeValue(convertedDocument, "select", "id", 1))
        assertEquals("String", attributeValue(convertedDocument, "select", "resultType", 1))
        assertEquals("long", attributeValue(convertedDocument, "select", "parameterType", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 1))
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
                WHERE
                  id = #id#
              </select>
              <select id="find2" resultClass="String" parameterClass="long">
                SELECT
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
