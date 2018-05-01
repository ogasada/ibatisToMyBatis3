package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNotNullTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val isNotNullTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotNull")
        assertEquals(2, isNotNullTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotNull", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNotNull", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotNull", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotNull", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotNull", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotNull", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotNull", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotNull", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotNull", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotNull", "close", 1))

        val convertedDocument = IsNotNullTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("name != null", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("name2 != null", attributeValue(convertedDocument, "if", "test", 1))
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
                <isNotNull prepend="and" open="(" property="name" close=")">
                  name = #name#
                  <isNotNull prepend="or" property="name2">
                    name = #name2#
                  </isNotNull>
                </isNotNull>
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
