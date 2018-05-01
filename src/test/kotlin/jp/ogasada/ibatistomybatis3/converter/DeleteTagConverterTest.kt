package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class DeleteTagConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        val deleteTagsBeforeConvert = loadedDocument.getElementsByTagName("delete")
        assertEquals(1, deleteTagsBeforeConvert.length)
        assertEquals("delete", attributeValue(loadedDocument, "delete", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(loadedDocument, "delete", "parameterClass", 0))
        assertFalse(existsAttribute(loadedDocument, "delete", "parameterType", 0))

        val convertedDocument = DeleteTagConverter.convert(loadedDocument)

        val deleteTagsAfterConvert = convertedDocument.getElementsByTagName("delete")
        assertEquals(1, deleteTagsAfterConvert.length)
        assertEquals("delete", attributeValue(convertedDocument, "delete", "id", 0))
        assertEquals("jp.ogasada.ibatistomybatis3.TestTableEntity", attributeValue(convertedDocument, "delete", "parameterType", 0))
        assertFalse(existsAttribute(convertedDocument, "delete", "parameterClass", 0))
    }

    private fun loadValidDocument(): Document {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE sqlMap
              PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
              "http://ibatis.apache.org/dtd/sql-map-2.dtd">

            <sqlMap namespace="jp.ogasada.test">
              <delete id="delete" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                DELETE FROM testTable
                WHERE
                  id = #id#
              </delete></sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
