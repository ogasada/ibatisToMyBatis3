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
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE sqlMap\n" +
                "  PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\"\n" +
                "  \"http://ibatis.apache.org/dtd/sql-map-2.dtd\">\n" +
                "\n" +
                "<sqlMap namespace=\"jp.ogasada.test\">\n" +
                "  <resultMap id=\"findResult\" class=\"HashMap\" groupBy=\"id\">\n" +
                "    <result property=\"id\" column=\"id\" javaType=\"int\" />\n" +
                "    <result property=\"name\" column=\"name\" javaType=\"String\" />\n" +
                "    <result property=\"detailList\" javaType=\"List\" resultMap=\"jp.ogasada.ibatistomybatis3.detailResult\" />\n" +
                "  </resultMap>\n" +
                "  <select id=\"find\" resultMap=\"findResult\" parameterClass=\"long\">\n" +
                "    SELECT\n" +
                "      id,\n" +
                "      name\n" +
                "    FROM\n" +
                "      testTable\n" +
                "    <dynamic prepend=\"where\" open=\"(\" close=\")\">\n" +
                "      <isEqual prepend=\"and\" property=\"id\" compareValue=\"1\">\n" +
                "        id = #id#\n" +
                "      </isEqual>\n" +
                "      <isEqual prepend=\"and\" property=\"name\" compareValue=\"foo\">\n" +
                "        name = #name#\n" +
                "      </isEqual>\n" +
                "    </dynamic>\n" +
                "  </select>\n" +
                "  <update id=\"update\" parameterClass=\"jp.ogasada.ibatistomybatis3.TestTableEntity\" >\n" +
                "    UPDATE testTable\n" +
                "    <dynamic prepend=\"set\">\n" +
                "      <isNotNull prepend=\",\" property=\"id\">\n" +
                "        id = #id#\n" +
                "      </isNotNull>\n" +
                "      <isNotNull prepend=\",\" property=\"name\">\n" +
                "        name = #name#\n" +
                "      </isNotNull>\n" +
                "    </dynamic>\n" +
                "    WHERE\n" +
                "      key = #key#\n" +
                "  </update>\n" +
                "</sqlMap>\n"

        return loadXML(xml)
    }
}
