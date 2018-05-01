package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class UpdateTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val updateTagsBeforeConvert = loadedDocument.getElementsByTagName("update")
        assertEquals(1, updateTagsBeforeConvert.length)
        assertEquals("update", attributeValue(loadedDocument, "update", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "update", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "update", "parameterType", 0))

        val convertedDocument = UpdateTagConverter.convert(loadedDocument)

        val updateTagsAfterConvert = convertedDocument.getElementsByTagName("update")
        assertEquals(1, updateTagsAfterConvert.length)
        assertEquals("update", attributeValue(convertedDocument, "update", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "update", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "update", "parameterClass", 0))
    }

    private fun loadValidDocument(): Document {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE sqlMap
              PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
              "http://ibatis.apache.org/dtd/sql-map-2.dtd">

            <sqlMap namespace="jp.ogasada.test">
              <update id="update" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                UPDATE testTable SET
                  name = #name#
                WHERE
                  id = #id#
              </update></sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
