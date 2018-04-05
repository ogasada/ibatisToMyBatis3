package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class SqlMapFileConverterTest {
    @Test
    fun convert() {
        val loadedDocument = loadValidDocument()

        val sqlMapTagsBeforeConvert = loadedDocument.getElementsByTagName("sqlMap")
        assertEquals(1, sqlMapTagsBeforeConvert.length)
        val mapperTagsBeforeConvert = loadedDocument.getElementsByTagName("mapper")
        assertEquals(0, mapperTagsBeforeConvert.length)
        assertTrue(existsAttribute(loadedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "id"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "groupBy"))
        assertFalse(existsAttribute(loadedDocument, "resultMap", "type"))
        assertEquals(attributeValue(loadedDocument, "resultMap", "id"), "findResult")
        assertEquals(attributeValue(loadedDocument, "resultMap", "class"), "HashMap")
        assertEquals(attributeValue(loadedDocument, "resultMap", "groupBy"), "id")

        val convertedDocument: Document = SqlMapFileConverter.convert(loadedDocument)

        val sqlMapTagsAfterConvert = convertedDocument.getElementsByTagName("sqlMap")
        assertEquals(0, sqlMapTagsAfterConvert.length)
        val mapperTagsAfterConvert = convertedDocument.getElementsByTagName("mapper")
        assertEquals(1, mapperTagsAfterConvert.length)
        assertFalse(existsAttribute(loadedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "id"))
        assertFalse(existsAttribute(loadedDocument, "resultMap", "groupBy"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "type"))
        assertEquals(attributeValue(loadedDocument, "resultMap", "id"), "findResult")
        assertEquals(attributeValue(loadedDocument, "resultMap", "type"), "HashMap")
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
                "</sqlMap>\n"

        return loadXML(xml)
    }
}
