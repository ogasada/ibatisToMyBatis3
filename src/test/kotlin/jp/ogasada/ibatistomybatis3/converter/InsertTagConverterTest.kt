package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class InsertTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val insertTagsBeforeConvert = loadedDocument.getElementsByTagName("insert")
        assertEquals(1, insertTagsBeforeConvert.length)
        assertEquals("insert", attributeValue(loadedDocument, "insert", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "insert", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "insert", "parameterType", 0))

        val convertedDocument = InsertTagConverter.convert(loadedDocument)

        val insertTagsAfterConvert = convertedDocument.getElementsByTagName("insert")
        assertEquals(1, insertTagsAfterConvert.length)
        assertEquals("insert", attributeValue(convertedDocument, "insert", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "insert", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "insert", "parameterClass", 0))
    }

    private fun loadValidDocument(): Document {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE sqlMap\n" +
                "  PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\"\n" +
                "  \"http://ibatis.apache.org/dtd/sql-map-2.dtd\">\n" +
                "\n" +
                "<sqlMap namespace=\"jp.ogasada.test\">\n" +
                "  <insert id=\"insert\" parameterClass=\"jp.ogasada.ibatistomybatis3.TestTableEntity\" >\n" +
                "    INSERT INTO testTable (\n" +
                "      id,\n" +
                "      name\n" +
                "    )\n" +
                "    VALUES (\n" +
                "      #id#,\n" +
                "      #name#\n" +
                "    )\n" +
                "  </insert>" +
                "</sqlMap>\n"

        return loadXML(xml)
    }
}
