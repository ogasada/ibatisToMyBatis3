package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNullTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val isNullTagsBeforeConvert = loadedDocument.getElementsByTagName("isNull")
        assertEquals(2, isNullTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNull", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNull", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNull", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNull", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNull", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNull", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNull", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNull", 1))
        assertFalse(existsAttribute(loadedDocument, "isNull", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNull", "close", 1))

        val convertedDocument = IsNullTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("name == null", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("name2 == null", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
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
                  id = #id#
                <isNull prepend="and" open="(" property="name" close=")">
                  name = #name#
                  <isNull prepend="or" property="name2">
                    name = #name2#
                  </isNull>
                </isNull>
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
