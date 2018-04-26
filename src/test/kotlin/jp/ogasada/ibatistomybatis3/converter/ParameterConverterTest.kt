package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

internal class ParameterConverterTest {
    @Test
    fun convertForValidDocument() {
        val loadedDocument = loadValidDocument()

        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "if", 1))
        assertEquals("\n        name = '\$name\$'\n      ", textContent(loadedDocument, "if", 3))
        assertEquals("\n        name = '\$name\$ || #id#'\n      ", textContent(loadedDocument, "if", 4))
        assertEquals("\n      id = #id#\n    ", textContent(loadedDocument, "if", 5))

        val convertedDocument = ParameterConverter.convert(loadedDocument)

        assertEquals("\n      id = #{_parameter}\n    ", textContent(convertedDocument, "if", 1))
        assertEquals("\n        name = '\${name}'\n      ", textContent(convertedDocument, "if", 3))
        assertEquals("\n        name = '\${name} || #{id}'\n      ", textContent(convertedDocument, "if", 4))
        assertEquals("\n      id = #{id}\n    ", textContent(convertedDocument, "if", 5))
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
                    <if test="_parameter == ''">
                      id = 0
                    </if>
                    <if test="_parameter != ''">
                      id = #id#
                    </if>
                  </select>
                  <update id="update" parameterType="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                    UPDATE testTable
                    SET
                    <if test="id == 0">
                      <if test=" name == ''">
                        name = '${"$"}name$'
                      </if>
                      <if test=" name != ''">
                        name = '${"$"}name$ || #id#'
                      </if>
                    </if>
                    <if test="id gt 0">
                      id = #id#
                    </if>
                    WHERE
                    key = #key#
                  </update>
                </mapper>
                """.trimIndent()

        return loadXML(xml)
    }
}
