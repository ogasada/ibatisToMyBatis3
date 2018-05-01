package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsEmptyTagConverterTest {
    @Test
    fun convertForValidDocumentWithProperty() {
        val loadedDocument = loadValidDocumentWithProperty()

        val isEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isEmpty")
        assertEquals(1, isEmptyTagsBeforeConvert.length)
        assertEquals("or", attributeValue(loadedDocument, "isEmpty", "prepend", 0))
        assertEquals("name", attributeValue(loadedDocument, "isEmpty", "property", 0))
        assertEquals("\n        name ISNULL\n      ", textContent(loadedDocument, "isEmpty", 0))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "open", 0))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "close", 0))

        val convertedDocument = IsEmptyTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals(''))", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n         or name ISNULL\n      ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))
    }

    @Test
    fun convertForValidDocumentWithNoProperty() {
        val loadedDocument = loadValidDocumentWithNoProperty()

        val isEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isEmpty")
        assertEquals(1, isEmptyTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isEmpty", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isEmpty", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isEmpty", "close", 0))
        assertEquals("\n      name ISNULL or name = ''\n    ", textContent(loadedDocument, "isEmpty", 0))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "property", 0))

        val convertedDocument = IsEmptyTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(1, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('_parameter')) or _parameter == null or (_parameter instanceof java.util.Collection and _parameter.size() == 0) or (_parameter.getClass().isArray() and @java.lang.reflect.Array@getLength(_parameter) == 0) or (_parameter instanceof String and _parameter.equals(''))", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name ISNULL or name = '' ) \n    ", textContent(convertedDocument, "if", 0))
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
              <select id="find" resultClass="String" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity">
                SELECT
                  name
                FROM
                  testTable
                WHERE
                  id = #id#
                  <isEmpty prepend="or" property="name">
                    name ISNULL
                  </isEmpty>
              </select>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }

    private fun loadValidDocumentWithNoProperty(): Document {
        val xml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE sqlMap
                    PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
                    "http://ibatis.apache.org/dtd/sql-map-2.dtd">

                <sqlMap namespace="jp.ogasada.test">
                  <resultMap id="findResult" class="HashMap" groupBy="id">
                    <result property="id" column="id" javaType="int" />
                    <result property="name" column="name" javaType="String" />
                  </resultMap>
                  <select id="find2" resultMap="findResult" parameterClass="String">
                    SELECT
                      id,
                      name
                    FROM
                      testTable
                    WHERE
                      id BETWEEN 1 AND 100
                    <isEmpty prepend="and" open="(" close=")">
                      name ISNULL or name = ''
                    </isEmpty>
                  </select>
                </sqlMap>
                """.trimIndent()

        return loadXML(xml)
    }
}
