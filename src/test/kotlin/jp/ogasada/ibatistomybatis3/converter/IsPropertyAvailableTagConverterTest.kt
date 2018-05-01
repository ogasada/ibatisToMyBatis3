package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsPropertyAvailableTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isPropertyAvailable")
        assertEquals(2, isPropertyAvailableTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isPropertyAvailable", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isPropertyAvailable", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isPropertyAvailable", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isPropertyAvailable", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isPropertyAvailable", 0))

        assertEquals("or", attributeValue(loadedDocument, "isPropertyAvailable", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isPropertyAvailable", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isPropertyAvailable", 1))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "close", 1))

        val convertedDocument = IsPropertyAvailableTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name'))", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name2'))", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
    }

    @Test
    fun convertForValidDocumentWithNoProperty() {
        val loadedDocument = loadValidDocumentWithNoProperty()

        val isPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isPropertyAvailable")
        assertEquals(1, isPropertyAvailableTagsBeforeConvert.length)
        assertEquals("or", attributeValue(loadedDocument, "isPropertyAvailable", "prepend", 0))
        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "isPropertyAvailable", 0))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "close", 0))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "property", 0))

        val convertedDocument = IsPropertyAvailableTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('_parameter'))", attributeValue(convertedDocument, "if", "test", 0))
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
                <isPropertyAvailable prepend="and" open="(" property="name" close=")">
                  name = #name#
                  <isPropertyAvailable prepend="or" property="name2">
                    name = #name2#
                  </isPropertyAvailable>
                </isPropertyAvailable>
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
                <isPropertyAvailable prepend="or">
                  id = #id#
                </isPropertyAvailable>
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
