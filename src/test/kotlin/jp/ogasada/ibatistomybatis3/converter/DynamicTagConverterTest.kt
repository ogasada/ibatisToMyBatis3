package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class DynamicTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val dynamicTagsBeforeConvert = loadedDocument.getElementsByTagName("dynamic")
        assertEquals(2, dynamicTagsBeforeConvert.length)
        assertEquals("where", attributeValue(loadedDocument, "dynamic", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "dynamic", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "dynamic", "close", 0))
        assertEquals("set", attributeValue(loadedDocument, "dynamic", "prepend", 1))

        val convertedDocument = DynamicTagConverter.convert(loadedDocument)

        val trimTagsAfterConvert = convertedDocument.getElementsByTagName("trim")
        assertEquals(2, trimTagsAfterConvert.length)
        assertEquals("where (", attributeValue(convertedDocument, "trim", "prefix", 0))
        assertEquals(")", attributeValue(convertedDocument, "trim", "suffix", 0))
        assertEquals("AND |OR ", attributeValue(convertedDocument, "trim", "prefixOverrides", 0))

        assertEquals("set", attributeValue(convertedDocument, "trim", "prefix", 1))
        assertEquals(", |AND |OR ", attributeValue(convertedDocument, "trim", "prefixOverrides", 1))

        assertFalse(existsAttribute(convertedDocument, "trim", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "trim", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "trim", "close", 0))

        assertFalse(existsAttribute(convertedDocument, "trim", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "trim", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "trim", "close", 1))
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
                <dynamic prepend="where" open="(" close=")">
                  <isEqual prepend="and" property="id" compareValue="1">
                    id = #id#
                  </isEqual>
                  <isEqual prepend="and" property="name" compareValue="foo">
                    name = #name#
                  </isEqual>
                </dynamic>
              </select>
              <update id="update" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                UPDATE testTable
                <dynamic prepend="set">
                  <isNotNull prepend="," property="id">
                    id = #id#
                  </isNotNull>
                  <isNotNull prepend="," property="name">
                    name = #name#
                  </isNotNull>
                </dynamic>
                WHERE
                  key = #key#
              </update>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
