package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsEqualTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isEqual")
        assertEquals(2, isEqualTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isEqual", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isEqual", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isEqual", "property", 0))
        assertEquals("foo", attributeValue(loadedDocument, "isEqual", "compareValue", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isEqual", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isEqual", "property", 1))
        assertEquals("compareName2", attributeValue(loadedDocument, "isEqual", "compareProperty", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "compareValue", 1))

        val convertedDocument = IsEqualTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name'))) and name.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name2'))) and name2.toString().equals(compareName2.toString())", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
    }

    @Test
    fun convertForValidDocumentWithNoProperty() {
        val loadedDocument = loadValidDocumentWithNoProperty()

        val isEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isEqual")
        assertEquals(1, isEqualTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isEqual", "prepend", 0))
        assertEquals("foo", attributeValue(loadedDocument, "isEqual", "compareValue", 0))
        assertEquals("\n      name = #name#\n    ", textContent(loadedDocument, "isEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "compareProperty", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "close", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "property", 0))

        val convertedDocument = IsEqualTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('_parameter'))) and _parameter.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n       and name = #name#\n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 0))
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
              <select id="find" resultMap="findResult" parameterClass="String">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = 0
                <isEqual prepend="and" open="(" property="name" compareValue="foo" close=")">
                  name = #name#
                  <isEqual prepend="or" property="name2" compareProperty="compareName2">
                    name = #name2#
                  </isEqual>
                </isEqual>
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
              <select id="find" resultMap="findResult" parameterClass="String">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = 0
                <isEqual prepend="and" compareValue="foo">
                  name = #name#
                </isEqual>
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
