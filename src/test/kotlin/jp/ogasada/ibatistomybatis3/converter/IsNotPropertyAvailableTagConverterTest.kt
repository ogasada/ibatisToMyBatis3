package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsNotPropertyAvailableTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val isNotPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotPropertyAvailable")
        assertEquals(2, isNotPropertyAvailableTagsBeforeConvert.length)
        assertEquals("and", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 0))
        assertEquals("(", attributeValue(loadedDocument, "isNotPropertyAvailable", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotPropertyAvailable", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 0))
        assertEquals("\n      name = #name3#\n      \n    ", textContent(loadedDocument, "isNotPropertyAvailable", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 1))
        assertEquals("\n        name = #name4#\n      ", textContent(loadedDocument, "isNotPropertyAvailable", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "close", 1))

        val convertedDocument = IsNotPropertyAvailableTagConverter.convert(loadedDocument)

        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(2, ifTagsAfterConvert.length)
        assertEquals("!_parameter.containsKey('name')", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name3#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("!_parameter.containsKey('name2')", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name4#\n      ", textContent(convertedDocument, "if", 1))
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
                "    <isNotPropertyAvailable prepend=\"and\" open=\"(\" property=\"name\" close=\")\">\n" +
                "      name = #name3#\n" +
                "      <isNotPropertyAvailable prepend=\"or\" property=\"name2\">\n" +
                "        name = #name4#\n" +
                "      </isNotPropertyAvailable>\n" +
                "    </isNotPropertyAvailable>\n" +
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
