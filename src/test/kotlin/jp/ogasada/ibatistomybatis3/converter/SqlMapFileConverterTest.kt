package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class SqlMapFileConverterTest {
    @Test
    fun convert() {
        val loadedDocument = loadValidDocument()

        sqlMapTagTestBeforeConvert(loadedDocument)
        resultMapTagTestBeforeConvert(loadedDocument)
        resultTagTestBeforeConvert(loadedDocument)
        selectTagTestBeforeConvert(loadedDocument)
        insertTagTestBeforeConvert(loadedDocument)
        updateTagTestBeforeConvert(loadedDocument)
        deleteTagTestBeforeConvert(loadedDocument)

        val convertedDocument: Document = SqlMapFileConverter.convert(loadedDocument)

        sqlMapTagTestAfterConvert(convertedDocument)
        resultMapTagTestAfterConvert(convertedDocument)
        resultTagTestAfterConvert(convertedDocument)
        selectTagTestAfterConvert(convertedDocument)
        insertTagTestAfterConvert(convertedDocument)
        updateTagTestAfterConvert(convertedDocument)
        deleteTagTestAfterConvert(convertedDocument)
    }

    private fun sqlMapTagTestBeforeConvert(loadedDocument: Document) {
        val sqlMapTagsBeforeConvert = loadedDocument.getElementsByTagName("sqlMap")
        assertEquals(1, sqlMapTagsBeforeConvert.length)
        val mapperTagsBeforeConvert = loadedDocument.getElementsByTagName("mapper")
        assertEquals(0, mapperTagsBeforeConvert.length)
    }

    private fun sqlMapTagTestAfterConvert(convertedDocument: Document) {
        val sqlMapTagsAfterConvert = convertedDocument.getElementsByTagName("sqlMap")
        assertEquals(0, sqlMapTagsAfterConvert.length)
        val mapperTagsAfterConvert = convertedDocument.getElementsByTagName("mapper")
        assertEquals(1, mapperTagsAfterConvert.length)
    }

    private fun resultMapTagTestBeforeConvert(loadedDocument: Document) {
        assertTrue(existsAttribute(loadedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "id"))
        assertTrue(existsAttribute(loadedDocument, "resultMap", "groupBy"))
        assertFalse(existsAttribute(loadedDocument, "resultMap", "type"))
        assertEquals("findResult", attributeValue(loadedDocument, "resultMap", "id"))
        assertEquals("HashMap", attributeValue(loadedDocument, "resultMap", "class"))
        assertEquals("id", attributeValue(loadedDocument, "resultMap", "groupBy"))
    }

    private fun resultMapTagTestAfterConvert(convertedDocument: Document) {
        assertFalse(existsAttribute(convertedDocument, "resultMap", "class"))
        assertTrue(existsAttribute(convertedDocument, "resultMap", "id"))
        assertFalse(existsAttribute(convertedDocument, "resultMap", "groupBy"))
        assertTrue(existsAttribute(convertedDocument, "resultMap", "type"))
        assertEquals("findResult", attributeValue(convertedDocument, "resultMap", "id"))
        assertEquals("HashMap", attributeValue(convertedDocument, "resultMap", "type"))
    }

    private fun resultTagTestBeforeConvert(loadedDocument: Document) {
        assertEquals("id", attributeValue(loadedDocument, "result", "property", 0))
        assertEquals("name", attributeValue(loadedDocument, "result", "property", 1))
        assertEquals("detailList", attributeValue(loadedDocument, "result", "property", 2))
        assertEquals("id", attributeValue(loadedDocument, "result", "column", 0))
        assertEquals("name", attributeValue(loadedDocument, "result", "column", 1))
        assertEquals("int", attributeValue(loadedDocument, "result", "javaType", 0))
        assertEquals("String", attributeValue(loadedDocument, "result", "javaType", 1))
        assertEquals("List", attributeValue(loadedDocument, "result", "javaType", 2))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 0))
        assertFalse(existsAttribute(loadedDocument, "result", "resultMap", 1))
        assertTrue(existsAttribute(loadedDocument, "result", "resultMap", 2))
        assertEquals("jp.ogasada.ibatistomybatis3.detailResult", attributeValue(loadedDocument, "result", "resultMap", 2))
    }

    private fun resultTagTestAfterConvert(convertedDocument: Document) {
        assertEquals("id", attributeValue(convertedDocument, "result", "property", 0))
        assertEquals("name", attributeValue(convertedDocument, "result", "property", 1))
        assertEquals("detailList", attributeValue(convertedDocument, "result", "property", 2))
        assertEquals("id", attributeValue(convertedDocument, "result", "column", 0))
        assertEquals("name", attributeValue(convertedDocument, "result", "column", 1))
        assertEquals("int", attributeValue(convertedDocument, "result", "javaType", 0))
        assertEquals("String", attributeValue(convertedDocument, "result", "javaType", 1))
        assertEquals("List", attributeValue(convertedDocument, "result", "javaType", 2))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 0))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 1))
        assertTrue(existsAttribute(convertedDocument, "result", "resultMap", 2))
        assertEquals("detailResult", attributeValue(convertedDocument, "result", "resultMap", 2))
    }

    private fun selectTagTestBeforeConvert(loadedDocument: Document) {
        val selectTagsBeforeConvert = loadedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsBeforeConvert.length)
        assertEquals("find", attributeValue(loadedDocument, "select", "id", 0))
        assertEquals("findResult", attributeValue(loadedDocument, "select", "resultMap", 0))
        assertEquals("long", attributeValue(loadedDocument, "select", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 0))
        assertEquals("find2", attributeValue(loadedDocument, "select", "id", 1))
        assertEquals("String", attributeValue(loadedDocument, "select", "resultClass", 1))
        assertEquals("long", attributeValue(loadedDocument, "select", "parameterClass", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "resultType", 1))
        assertFalse(existsAttribute(loadedDocument, "select", "parameterType", 1))
    }

    private fun selectTagTestAfterConvert(convertedDocument: Document) {
        val selectTagsAfterConvert = convertedDocument.getElementsByTagName("select")
        assertEquals(2, selectTagsAfterConvert.length)
        assertEquals("find", attributeValue(convertedDocument, "select", "id", 0))
        assertEquals("findResult", attributeValue(convertedDocument, "select", "resultMap", 0))
        assertEquals("long", attributeValue(convertedDocument, "select", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "resultType", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 0))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 0))
        assertEquals("find2", attributeValue(convertedDocument, "select", "id", 1))
        assertEquals("String", attributeValue(convertedDocument, "select", "resultType", 1))
        assertEquals("long", attributeValue(convertedDocument, "select", "parameterType", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "resultMap", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "resultClass", 1))
        assertFalse(existsAttribute(convertedDocument, "select", "parameterClass", 1))
    }

    private fun insertTagTestBeforeConvert(loadedDocument: Document) {
        val insertTagsBeforeConvert = loadedDocument.getElementsByTagName("insert")
        assertEquals(1, insertTagsBeforeConvert.length)
        assertEquals("insert", attributeValue(loadedDocument, "insert", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "insert", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "insert", "parameterType", 0))
    }

    private fun insertTagTestAfterConvert(convertedDocument: Document) {
        val insertTagsAfterConvert = convertedDocument.getElementsByTagName("insert")
        assertEquals(1, insertTagsAfterConvert.length)
        assertEquals("insert", attributeValue(convertedDocument, "insert", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "insert", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "insert", "parameterClass", 0))
    }

    private fun updateTagTestBeforeConvert(loadedDocument: Document) {
        val updateTagsBeforeConvert = loadedDocument.getElementsByTagName("update")
        assertEquals(1, updateTagsBeforeConvert.length)
        assertEquals("update", attributeValue(loadedDocument, "update", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "update", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "update", "parameterType", 0))
    }

    private fun updateTagTestAfterConvert(convertedDocument: Document) {
        val updateTagsAfterConvert = convertedDocument.getElementsByTagName("update")
        assertEquals(1, updateTagsAfterConvert.length)
        assertEquals("update", attributeValue(convertedDocument, "update", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "update", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "update", "parameterClass", 0))
    }

    private fun deleteTagTestBeforeConvert(loadedDocument: Document) {
        val deleteTagsBeforeConvert = loadedDocument.getElementsByTagName("delete")
        assertEquals(1, deleteTagsBeforeConvert.length)
        assertEquals("delete", attributeValue(loadedDocument, "delete", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "delete", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "delete", "parameterType", 0))
    }

    private fun deleteTagTestAfterConvert(convertedDocument: Document) {
        val deleteTagsAfterConvert = convertedDocument.getElementsByTagName("delete")
        assertEquals(1, deleteTagsAfterConvert.length)
        assertEquals("delete", attributeValue(convertedDocument, "delete", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "delete", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "delete", "parameterClass", 0))
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
                "  <update id=\"update\" parameterClass=\"jp.ogasada.ibatistomybatis3.TestTableEntity\" >\n" +
                "    UPDATE testTable SET\n" +
                "      name = #name# \n" +
                "    WHERE \n" +
                "      id = #id#\n" +
                "  </update>" +
                "  <delete id=\"delete\" parameterClass=\"jp.ogasada.ibatistomybatis3.TestTableEntity\" >\n" +
                "    DELETE FROM testTable \n" +
                "    WHERE \n" +
                "      id = #id#\n" +
                "  </delete>" +
                "</sqlMap>\n"

        return loadXML(xml)
    }
}
