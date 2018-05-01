package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class ParameterConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        assertEquals("\n    SELECT\n      id,\n      name\n    FROM\n      testTable\n    WHERE\n      id = #id# or\n      id = \$id$\n  ", textContent(loadedDocument, "select", 0))
        assertEquals("\n    UPDATE testTable\n    SET\n      name = '\$name$ || #id#'\n      , id = #id#\n    WHERE\n      key = #key#\n  ", textContent(loadedDocument, "update", 0))

        val convertedDocument = ParameterConverter.convert(loadedDocument)

        assertEquals("\n    SELECT\n      id,\n      name\n    FROM\n      testTable\n    WHERE\n      id = #{id} or\n      id = \${value}\n  ", textContent(convertedDocument, "select", 0))
        assertEquals("\n    UPDATE testTable\n    SET\n      name = '\${name} || #{id}'\n      , id = #{id}\n    WHERE\n      key = #{key}\n  ", textContent(convertedDocument, "update", 0))
    }

    private fun loadValidDocument(): Document {
        val xml =
                """
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                <mapper namespace="jp.ogasada.test">
                  <resultMap id="findResult" type="HashMap">
                    <result property="id" column="id" javaType="int" />
                    <result property="name" column="name" javaType="String" />
                  </resultMap>
                  <select id="find" resultMap="findResult" parameterType="long">
                    SELECT
                      id,
                      name
                    FROM
                      testTable
                    WHERE
                      id = #id# or
                      id = ${"$"}id$
                  </select>
                  <update id="update" parameterType="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                    UPDATE testTable
                    SET
                      name = '${"$"}name$ || #id#'
                      , id = #id#
                    WHERE
                      key = #key#
                  </update>
                </mapper>
                """.trimIndent()

        return loadXML(xml)
    }
}
