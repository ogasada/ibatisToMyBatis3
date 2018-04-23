package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class ParameterConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        assertEquals("\n        id = #id#\n      ", textContent(loadedDocument, "isEqual", 0))
        assertEquals("\n        name = \$name\$\n      ", textContent(loadedDocument, "isEqual", 1))
        assertEquals("\n        id = #id# || #name#\n      ", textContent(loadedDocument, "isNotNull", 0))
        assertEquals("\n        name = \$id\$ || \$name\$\n      ", textContent(loadedDocument, "isNotNull", 1))

        val convertedDocument = ParameterConverter.convert(loadedDocument)

        assertEquals("\n        id = #{id}\n      ", textContent(convertedDocument, "isEqual", 0))
        assertEquals("\n        name = \${name}\n      ", textContent(convertedDocument, "isEqual", 1))
        assertEquals("\n        id = #{id} || #{name}\n      ", textContent(convertedDocument, "isNotNull", 0))
        assertEquals("\n        name = \${id} || \${name}\n      ", textContent(convertedDocument, "isNotNull", 1))
    }

    private fun loadValidDocument(): Document {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE sqlMap\n" +
                "  PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\"\n" +
                "  \"http://ibatis.apache.org/dtd/sql-map-2.dtd\">\n" +
                "\n" +
                "<mapper namespace=\"jp.ogasada.test\">\n" +
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
                "    <dynamic prepend=\"where\" open=\"(\" close=\")\">\n" +
                "      <isEqual prepend=\"and\" property=\"id\" compareValue=\"1\">\n" +
                "        id = #id#\n" +
                "      </isEqual>\n" +
                "      <isEqual prepend=\"and\" property=\"name\" compareValue=\"foo\">\n" +
                "        name = \$name\$\n" +
                "      </isEqual>\n" +
                "    </dynamic>\n" +
                "  </select>\n" +
                "  <update id=\"update\" parameterClass=\"jp.ogasada.ibatistomybatis3.TestTableEntity\" >\n" +
                "    UPDATE testTable\n" +
                "    <dynamic prepend=\"set\">\n" +
                "      <isNotNull prepend=\",\" property=\"id\">\n" +
                "        id = #id# || #name#\n" +
                "      </isNotNull>\n" +
                "      <isNotNull prepend=\",\" property=\"name\">\n" +
                "        name = \$id\$ || \$name\$\n" +
                "      </isNotNull>\n" +
                "    </dynamic>\n" +
                "    WHERE\n" +
                "      key = #key#\n" +
                "  </update>\n" +
                "</mapper>\n"

        return loadXML(xml)
    }
}
