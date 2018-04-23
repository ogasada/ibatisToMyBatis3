package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class IsEqualTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

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
        assertEquals("name.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n        and ( name = #name#\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("name2.toString().equals(compareName2.toString())", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #name2#\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 1))
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
                "    <isEqual prepend=\"and\" open=\"(\" property=\"name\" compareValue=\"foo\" close=\")\">\n" +
                "      name = #name#\n" +
                "      <isEqual prepend=\"or\" property=\"name2\" compareProperty=\"compareName2\">\n" +
                "        name = #name2#\n" +
                "      </isEqual>\n" +
                "    </isEqual>\n" +
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
