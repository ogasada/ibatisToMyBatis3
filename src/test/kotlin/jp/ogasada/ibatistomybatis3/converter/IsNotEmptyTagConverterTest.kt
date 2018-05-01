package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNotEmptyTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isNotEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEmpty")
        assertEquals(2, isNotEmptyTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotEmpty", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNotEmpty", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotEmpty", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotEmpty", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotEmpty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotEmpty", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotEmpty", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotEmpty", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "close", 1))

        val convertedDocument = IsNotEmptyTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals('')))", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or name2 == null or (name2 instanceof java.util.Collection and name2.size() == 0) or (name2.getClass().isArray() and @java.lang.reflect.Array@getLength(name2) == 0) or (name2 instanceof String and name2.equals('')))", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
    }

    @Test
    fun convertForValidDocumentWithNoProperty() {
        val loadedDocument = loadValidDocumentWithNoProperty()

        val isNotEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEmpty")
        assertEquals(1, isNotEmptyTagsBeforeConvert.length)
        assertEquals("or", attributeValue(loadedDocument, "isNotEmpty", "prepend", 0))
        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "isNotEmpty", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "property", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "close", 0))

        val convertedDocument = IsNotEmptyTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or _parameter == null or (_parameter instanceof java.util.Collection and _parameter.size() == 0) or (_parameter.getClass().isArray() and @java.lang.reflect.Array@getLength(_parameter) == 0) or (_parameter instanceof String and _parameter.equals('')))", attributeValue(convertedDocument, "if", "test", 0))
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
                <isNotEmpty prepend="and" open="(" property="name" close=")">
                  name = #name#
                  <isNotEmpty prepend="or" property="name2">
                    name = #name2#
                  </isNotEmpty>
                </isNotEmpty>
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
                <isNotEmpty prepend="or">
                  id = #id#
                </isNotEmpty>
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
