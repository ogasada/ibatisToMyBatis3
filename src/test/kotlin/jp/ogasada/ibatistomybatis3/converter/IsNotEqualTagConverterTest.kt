package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNotEqualTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isNotEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEqual")
        assertEquals(2, isNotEqualTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotEqual", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNotEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotEqual", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotEqual", "property", 0))
        assertEquals("foo", attributeValue(loadedDocument, "isNotEqual", "compareValue", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotEqual", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotEqual", "property", 1))
        assertEquals("compareName2", attributeValue(loadedDocument, "isNotEqual", "compareProperty", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "compareValue", 1))

        val convertedDocument = IsNotEqualTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or !name.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or !name2.toString().equals(compareName2.toString())", attributeValue(convertedDocument, "if", "test", 1))
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

        val isNotEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEqual")
        assertEquals(1, isNotEqualTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotEqual", "prepend", 0))
        assertEquals("0", attributeValue(loadedDocument, "isNotEqual", "compareValue", 0))
        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "isNotEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "compareProperty", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "property", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "close", 0))

        val convertedDocument = IsNotEqualTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or !_parameter.toString().equals('0'.toString())", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n       and id = #id#\n    ", textContent(convertedDocument, "if", 0))
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
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = #id#
                <isNotEqual prepend="and" open="(" property="name" compareValue="foo" close=")">
                  name = #name#
                  <isNotEqual prepend="or" property="name2" compareProperty="compareName2">
                    name = #name2#
                  </isNotEqual>
                </isNotEqual>
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
              </resultMap>
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = 0
                <isNotEqual prepend="and" compareValue="0">
                  id = #id#
                </isNotEqual>
              </select>
            </sqlMap>
        """.trimIndent()

        return loadXML(xml)
    }
}
