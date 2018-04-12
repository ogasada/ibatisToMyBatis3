package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class SelectTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val selectTagsBeforeConvert = loadedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsBeforeConvert.length)
        assertEquals(attributeValue(loadedDocument, "select", "id", 0), "find")
        assertEquals(attributeValue(loadedDocument, "select", "resultMap", 0), "findResult")
        assertEquals(attributeValue(loadedDocument, "select", "parameterClass", 0), "long")
        assertFalse(existsAttribute(loadedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 0))
        assertEquals(attributeValue(loadedDocument, "select", "id", 1), "find2")
        assertEquals(attributeValue(loadedDocument, "select", "resultClass", 1), "String")
        assertEquals(attributeValue(loadedDocument, "select", "parameterClass", 1), "long")
        assertFalse(existsAttribute(loadedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 1))

        val convertedDocument = SelectTagConverter.convert(loadedDocument)

        val selectTagsAfterConvert = convertedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsAfterConvert.length)
        assertEquals(attributeValue(convertedDocument, "select", "id", 0), "find")
        assertEquals(attributeValue(convertedDocument, "select", "resultMap", 0), "findResult")
        assertEquals(attributeValue(convertedDocument, "select", "parameterType", 0), "long")
        assertFalse(existsAttribute(convertedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 0))
        assertEquals(attributeValue(convertedDocument, "select", "id", 1), "find2")
        assertEquals(attributeValue(convertedDocument, "select", "resultType", 1), "String")
        assertEquals(attributeValue(convertedDocument, "select", "parameterType", 1), "long")
        assertFalse(existsAttribute(convertedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 1))
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
                "    WHERE\n" +
                "      id = #id#\n" +
                "  </select>\n" +
                "  <select id=\"find2\" resultClass=\"String\" parameterClass=\"long\">\n" +
                "    SELECT\n" +
                "      name\n" +
                "    FROM\n" +
                "      testTable\n" +
                "    WHERE\n" +
                "      id = #id#\n" +
                "  </select>\n" +
                "</sqlMap>\n"

        return loadXML(xml)
    }
}
