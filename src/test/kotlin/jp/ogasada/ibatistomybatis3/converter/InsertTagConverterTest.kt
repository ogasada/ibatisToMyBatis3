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
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE sqlMap
              PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
              "http://ibatis.apache.org/dtd/sql-map-2.dtd">

            <sqlMap namespace="jp.ogasada.test">
              <insert id="insert" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                INSERT INTO testTable (
                  id,
                  name
                )
                VALUES (
                  #id#,
                  #name#
                )
              </insert></sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
