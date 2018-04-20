package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsPropertyAvailableTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

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
        assertEquals("_parameter.containsKey('name')", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("_parameter.containsKey('name2')", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
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
                "    <isPropertyAvailable prepend=\"and\" open=\"(\" property=\"name\" close=\")\">\n" +
                "      name = #name#\n" +
                "      <isPropertyAvailable prepend=\"or\" property=\"name2\">\n" +
                "        name = #name2#\n" +
                "      </isPropertyAvailable>\n" +
                "    </isPropertyAvailable>\n" +
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
