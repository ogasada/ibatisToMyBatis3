package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsLessThanTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val isLessThanTagsBeforeConvert = loadedDocument.getElementsByTagName("isLessThan")
        assertEquals(2, isLessThanTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isLessThan", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isLessThan", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isLessThan", "close", 0))
        assertEquals("id", attributeValue(loadedDocument, "isLessThan", "property", 0))
        assertEquals("1", attributeValue(loadedDocument, "isLessThan", "compareValue", 0))
        assertEquals("\n      id = #id#\n      \n    ", textContent(loadedDocument, "isLessThan", 0))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isLessThan", "prepend", 1))
        assertEquals("id2", attributeValue(loadedDocument, "isLessThan", "property", 1))
        assertEquals("compareId2", attributeValue(loadedDocument, "isLessThan", "compareProperty", 1))
        assertEquals("\n        id = #id2#\n      ", textContent(loadedDocument, "isLessThan", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "compareValue", 1))

        val convertedDocument = IsLessThanTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("id lt 1", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( id = #id#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("id2 lt compareId2", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or id = #id2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
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
                  name = #name#
                <isLessThan prepend="and" open="(" property="id" compareValue="1" close=")">
                  id = #id#
                  <isLessThan prepend="or" property="id2" compareProperty="compareId2">
                    id = #id2#
                  </isLessThan>
                </isLessThan>
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
