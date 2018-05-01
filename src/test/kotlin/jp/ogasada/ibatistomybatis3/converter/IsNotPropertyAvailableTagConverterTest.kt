package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNotPropertyAvailableTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isNotPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotPropertyAvailable")
        assertEquals(2, isNotPropertyAvailableTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNotPropertyAvailable", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotPropertyAvailable", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 0))
        assertEquals("\n      name = #name3#\n      \n    ", textContent(loadedDocument, "isNotPropertyAvailable", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 1))
        assertEquals("\n        name = #name4#\n      ", textContent(loadedDocument, "isNotPropertyAvailable", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "close", 1))

        val convertedDocument = IsNotPropertyAvailableTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name3#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name4#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
    }

    @Test
    fun convertForValidDocumentWithNoProperty() {
        val loadedDocument = loadValidDocumentWithNoProperty()

        val isNotPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotPropertyAvailable")
        assertEquals(1, isNotPropertyAvailableTagsBeforeConvert.length)
        assertEquals("or", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 0))
        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "isNotPropertyAvailable", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "property", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "close", 0))

        val convertedDocument = IsNotPropertyAvailableTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n       or id = #id#\n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))
    }

    private fun loadValidDocumentWithProperty(): Document {
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
                <isNotPropertyAvailable prepend="and" open="(" property="name" close=")">
                  name = #name3#
                  <isNotPropertyAvailable prepend="or" property="name2">
                    name = #name4#
                  </isNotPropertyAvailable>
                </isNotPropertyAvailable>
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

    private fun loadValidDocumentWithNoProperty(): Document {
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
                  id = 0
                <isNotPropertyAvailable prepend="or">
                  id = #id#
                </isNotPropertyAvailable>
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
