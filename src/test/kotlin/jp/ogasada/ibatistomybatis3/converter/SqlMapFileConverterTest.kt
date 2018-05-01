package jp.ogasada.ibatistomybatis3.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document

const val COUNT_OF_IF_TAG = 28

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
        isEmptyTagTestBeforeConvert(loadedDocument)
        isNotEmptyTagTestBeforeConvert(loadedDocument)
        isEqualTagTestBeforeConvert(loadedDocument)
        isNotEqualTagTestBeforeConvert(loadedDocument)
        isGreaterEqualTagTestBeforeConvert(loadedDocument)
        isGreaterThanTagTestBeforeConvert(loadedDocument)
        isLessEqualTagTestBeforeConvert(loadedDocument)
        isLessThanTagTestBeforeConvert(loadedDocument)
        isNullTagTestBeforeConvert(loadedDocument)
        isNotNullTagTestBeforeConvert(loadedDocument)
        isPropertyAvailableTagTestBeforeConvert(loadedDocument)
        isNotPropertyAvailableTagTestBeforeConvert(loadedDocument)
        dynamicTagTestBeforeConvert(loadedDocument)
        iterateTagTestBeforeConvert(loadedDocument)

        val convertedDocument: Document = SqlMapFileConverter.convert(loadedDocument)

        sqlMapTagTestAfterConvert(convertedDocument)
        resultMapTagTestAfterConvert(convertedDocument)
        resultTagTestAfterConvert(convertedDocument)
        selectTagTestAfterConvert(convertedDocument)
        insertTagTestAfterConvert(convertedDocument)
        updateTagTestAfterConvert(convertedDocument)
        deleteTagTestAfterConvert(convertedDocument)
        isEmptyTagTestAfterConvert(convertedDocument)
        isNotEmptyTagTestAfterConvert(convertedDocument)
        isEqualTagTestAfterConvert(convertedDocument)
        isNotEqualTagTestAfterConvert(convertedDocument)
        isGreaterEqualTagTestAfterConvert(convertedDocument)
        isGreaterThanTagTestAfterConvert(convertedDocument)
        isLessEqualTagTestAfterConvert(convertedDocument)
        isLessThanTagTestAfterConvert(convertedDocument)
        isNullTagTestAfterConvert(convertedDocument)
        isNotNullTagTestAfterConvert(convertedDocument)
        isPropertyAvailableTagTestAfterConvert(convertedDocument)
        isNotPropertyAvailableTagTestAfterConvert(convertedDocument)
        dynamicTagTestAfterConvert(convertedDocument)
        iterateTagTestAfterConvert(convertedDocument)
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
        assertEquals("id", attributeValue(convertedDocument, "result", "column", 0))
        assertEquals("name", attributeValue(convertedDocument, "result", "column", 1))
        assertEquals("int", attributeValue(convertedDocument, "result", "javaType", 0))
        assertEquals("String", attributeValue(convertedDocument, "result", "javaType", 1))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 0))
        assertFalse(existsAttribute(convertedDocument, "result", "resultMap", 1))
        assertEquals("detailList", attributeValue(convertedDocument, "collection", "property", 0))
        assertEquals("List", attributeValue(convertedDocument, "collection", "javaType", 0))
        assertEquals("detailResult", attributeValue(convertedDocument, "collection", "resultMap", 0))
    }

    private fun selectTagTestBeforeConvert(loadedDocument: Document) {
        val selectTagsBeforeConvert = loadedDocument.getElementsByTagName("select")
        assertEquals(3, selectTagsBeforeConvert.length)
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
        assertEquals(3, selectTagsAfterConvert.length)
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

    private fun isEmptyTagTestBeforeConvert(loadedDocument: Document) {
        val isEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isEmpty")
        assertEquals(2, isEmptyTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isEmpty", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isEmpty", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isEmpty", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isEmpty", 0))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isEmpty", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isEmpty", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isEmpty", 1))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isEmpty", "close", 1))
    }

    private fun isEmptyTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals(''))", attributeValue(convertedDocument, "if", "test", 0))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 0))

        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or name2 == null or (name2 instanceof java.util.Collection and name2.size() == 0) or (name2.getClass().isArray() and @java.lang.reflect.Array@getLength(name2) == 0) or (name2 instanceof String and name2.equals(''))", attributeValue(convertedDocument, "if", "test", 1))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 1))
    }

    private fun isNotEmptyTagTestBeforeConvert(loadedDocument: Document) {
        val isNotEmptyTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEmpty")
        assertEquals(2, isNotEmptyTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isNotEmpty", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotEmpty", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotEmpty", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotEmpty", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotEmpty", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotEmpty", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotEmpty", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEmpty", "close", 1))
    }

    private fun isNotEmptyTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or name == null or (name instanceof java.util.Collection and name.size() == 0) or (name.getClass().isArray() and @java.lang.reflect.Array@getLength(name) == 0) or (name instanceof String and name.equals('')))", attributeValue(convertedDocument, "if", "test", 2))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 2))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 2))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 2))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 2))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 2))

        assertEquals("!((!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or name2 == null or (name2 instanceof java.util.Collection and name2.size() == 0) or (name2.getClass().isArray() and @java.lang.reflect.Array@getLength(name2) == 0) or (name2 instanceof String and name2.equals('')))", attributeValue(convertedDocument, "if", "test", 3))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 3))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 3))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 3))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 3))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 3))
    }

    private fun isEqualTagTestBeforeConvert(loadedDocument: Document) {
        val isEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isEqual")
        assertEquals(4, isEqualTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isEqual", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isEqual", "property", 0))
        assertEquals("foo", attributeValue(loadedDocument, "isEqual", "compareValue", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isEqual", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isEqual", "property", 1))
        assertEquals("compareName2", attributeValue(loadedDocument, "isEqual", "compareProperty", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isEqual", "compareValue", 1))
    }

    private fun isEqualTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name'))) and name.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 4))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 4))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 4))

        assertEquals("((!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name2'))) and name2.toString().equals(compareName2.toString())", attributeValue(convertedDocument, "if", "test", 5))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 5))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 5))
    }

    private fun isNotEqualTagTestBeforeConvert(loadedDocument: Document) {
        val isNotEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotEqual")
        assertEquals(2, isNotEqualTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isNotEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotEqual", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotEqual", "property", 0))
        assertEquals("foo", attributeValue(loadedDocument, "isNotEqual", "compareValue", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotEqual", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotEqual", "property", 1))
        assertEquals("compareName2", attributeValue(loadedDocument, "isNotEqual", "compareProperty", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotEqual", "compareValue", 1))
    }

    private fun isNotEqualTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')) or !name.toString().equals('foo'.toString())", attributeValue(convertedDocument, "if", "test", 6))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 6))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 6))

        assertEquals("(!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')) or !name2.toString().equals(compareName2.toString())", attributeValue(convertedDocument, "if", "test", 7))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 7))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 7))
    }

    private fun isGreaterEqualTagTestBeforeConvert(loadedDocument: Document) {
        val isGreaterEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isGreaterEqual")
        assertEquals(2, isGreaterEqualTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isGreaterEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isGreaterEqual", "close", 0))
        assertEquals("id", attributeValue(loadedDocument, "isGreaterEqual", "property", 0))
        assertEquals("1", attributeValue(loadedDocument, "isGreaterEqual", "compareValue", 0))
        assertEquals("\n      id = #id#\n      \n    ", textContent(loadedDocument, "isGreaterEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isGreaterEqual", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isGreaterEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isGreaterEqual", "prepend", 1))
        assertEquals("id2", attributeValue(loadedDocument, "isGreaterEqual", "property", 1))
        assertEquals("compareId2", attributeValue(loadedDocument, "isGreaterEqual", "compareProperty", 1))
        assertEquals("\n        id = #id2#\n      ", textContent(loadedDocument, "isGreaterEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterEqual", "compareValue", 1))
    }

    private fun isGreaterEqualTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("id gte 1", attributeValue(convertedDocument, "if", "test", 8))
        assertEquals("\n       and ( id = #{id}\n       ) \n    ", textContent(convertedDocument, "if", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 8))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 8))

        assertEquals("id2 gte compareId2", attributeValue(convertedDocument, "if", "test", 9))
        assertEquals("\n         or id = #{id2}\n      ", textContent(convertedDocument, "if", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 9))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 9))
    }

    private fun isGreaterThanTagTestBeforeConvert(loadedDocument: Document) {
        val isGreaterThanTagsBeforeConvert = loadedDocument.getElementsByTagName("isGreaterThan")
        assertEquals(2, isGreaterThanTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isGreaterThan", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isGreaterThan", "close", 0))
        assertEquals("id", attributeValue(loadedDocument, "isGreaterThan", "property", 0))
        assertEquals("1", attributeValue(loadedDocument, "isGreaterThan", "compareValue", 0))
        assertEquals("\n      id = #id#\n      \n    ", textContent(loadedDocument, "isGreaterThan", 0))
        assertFalse(existsAttribute(loadedDocument, "isGreaterThan", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isGreaterThan", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isGreaterThan", "prepend", 1))
        assertEquals("id2", attributeValue(loadedDocument, "isGreaterThan", "property", 1))
        assertEquals("compareId2", attributeValue(loadedDocument, "isGreaterThan", "compareProperty", 1))
        assertEquals("\n        id = #id2#\n      ", textContent(loadedDocument, "isGreaterThan", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterThan", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterThan", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isGreaterThan", "compareValue", 1))
    }

    private fun isGreaterThanTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("id gt 1", attributeValue(convertedDocument, "if", "test", 10))
        assertEquals("\n       and ( id = #{id}\n       ) \n    ", textContent(convertedDocument, "if", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 10))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 10))

        assertEquals("id2 gt compareId2", attributeValue(convertedDocument, "if", "test", 11))
        assertEquals("\n         or id = #{id2}\n      ", textContent(convertedDocument, "if", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 11))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 11))
    }

    private fun isLessEqualTagTestBeforeConvert(loadedDocument: Document) {
        val isLessEqualTagsBeforeConvert = loadedDocument.getElementsByTagName("isLessEqual")
        assertEquals(2, isLessEqualTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isLessEqual", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isLessEqual", "close", 0))
        assertEquals("id", attributeValue(loadedDocument, "isLessEqual", "property", 0))
        assertEquals("1", attributeValue(loadedDocument, "isLessEqual", "compareValue", 0))
        assertEquals("\n      id = #id#\n      \n    ", textContent(loadedDocument, "isLessEqual", 0))
        assertFalse(existsAttribute(loadedDocument, "isLessEqual", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isLessEqual", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isLessEqual", "prepend", 1))
        assertEquals("id2", attributeValue(loadedDocument, "isLessEqual", "property", 1))
        assertEquals("compareId2", attributeValue(loadedDocument, "isLessEqual", "compareProperty", 1))
        assertEquals("\n        id = #id2#\n      ", textContent(loadedDocument, "isLessEqual", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessEqual", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessEqual", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessEqual", "compareValue", 1))
    }

    private fun isLessEqualTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("id lte 1", attributeValue(convertedDocument, "if", "test", 12))
        assertEquals("\n       and ( id = #{id}\n       ) \n    ", textContent(convertedDocument, "if", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 12))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 12))

        assertEquals("id2 lte compareId2", attributeValue(convertedDocument, "if", "test", 13))
        assertEquals("\n         or id = #{id2}\n      ", textContent(convertedDocument, "if", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 13))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 13))
    }

    private fun isLessThanTagTestBeforeConvert(loadedDocument: Document) {
        val isLessThanTagsBeforeConvert = loadedDocument.getElementsByTagName("isLessThan")
        assertEquals(2, isLessThanTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isLessThan", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isLessThan", "close", 0))
        assertEquals("id", attributeValue(loadedDocument, "isLessThan", "property", 0))
        assertEquals("1", attributeValue(loadedDocument, "isLessThan", "compareValue", 0))
        assertEquals("\n      id = #id#\n      \n    ", textContent(loadedDocument, "isLessThan", 0))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "prepend", 0))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "compareProperty", 0))

        assertEquals("or", attributeValue(loadedDocument, "isLessThan", "prepend", 1))
        assertEquals("id2", attributeValue(loadedDocument, "isLessThan", "property", 1))
        assertEquals("compareId2", attributeValue(loadedDocument, "isLessThan", "compareProperty", 1))
        assertEquals("\n        id = #id2#\n      ", textContent(loadedDocument, "isLessThan", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "close", 1))
        assertFalse(existsAttribute(loadedDocument, "isLessThan", "compareValue", 1))
    }

    private fun isLessThanTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("id lt 1", attributeValue(convertedDocument, "if", "test", 14))
        assertEquals("\n       and ( id = #{id}\n       ) \n    ", textContent(convertedDocument, "if", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 14))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 14))

        assertEquals("id2 lt compareId2", attributeValue(convertedDocument, "if", "test", 15))
        assertEquals("\n         or id = #{id2}\n      ", textContent(convertedDocument, "if", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "compareProperty", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "compareValue", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 15))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 15))
    }

    private fun isNullTagTestBeforeConvert(loadedDocument: Document) {
        val isNullTagsBeforeConvert = loadedDocument.getElementsByTagName("isNull")
        assertEquals(2, isNullTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isNull", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNull", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNull", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNull", 0))
        assertFalse(existsAttribute(loadedDocument, "isNull", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNull", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNull", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNull", 1))
        assertFalse(existsAttribute(loadedDocument, "isNull", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNull", "close", 1))
    }

    private fun isNullTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("name == null", attributeValue(convertedDocument, "if", "test", 16))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 16))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 16))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 16))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 16))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 16))

        assertEquals("name2 == null", attributeValue(convertedDocument, "if", "test", 17))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 17))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 17))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 17))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 17))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 17))
    }

    private fun isNotNullTagTestBeforeConvert(loadedDocument: Document) {
        val isNotNullTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotNull")
        assertEquals(4, isNotNullTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isNotNull", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotNull", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotNull", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isNotNull", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotNull", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotNull", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotNull", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isNotNull", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotNull", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotNull", "close", 1))
    }

    private fun isNotNullTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("name != null", attributeValue(convertedDocument, "if", "test", 18))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 18))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 18))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 18))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 18))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 18))

        assertEquals("name2 != null", attributeValue(convertedDocument, "if", "test", 19))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 19))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 19))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 19))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 19))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 19))
    }

    private fun isPropertyAvailableTagTestBeforeConvert(loadedDocument: Document) {
        val isPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isPropertyAvailable")
        assertEquals(2, isPropertyAvailableTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isPropertyAvailable", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isPropertyAvailable", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isPropertyAvailable", "property", 0))
        assertEquals("\n      name = #name#\n      \n    ", textContent(loadedDocument, "isPropertyAvailable", 0))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isPropertyAvailable", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isPropertyAvailable", "property", 1))
        assertEquals("\n        name = #name2#\n      ", textContent(loadedDocument, "isPropertyAvailable", 1))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isPropertyAvailable", "close", 1))
    }

    private fun isPropertyAvailableTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name'))", attributeValue(convertedDocument, "if", "test", 20))
        assertEquals("\n       and ( name = #{name}\n       ) \n    ", textContent(convertedDocument, "if", 20))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 20))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 20))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 20))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 20))

        assertEquals("(!_parameter instanceof java.util.Map) or (!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and _parameter.containsKey('name2'))", attributeValue(convertedDocument, "if", "test", 21))
        assertEquals("\n         or name = #{name2}\n      ", textContent(convertedDocument, "if", 21))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 21))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 21))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 21))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 21))
    }

    private fun isNotPropertyAvailableTagTestBeforeConvert(loadedDocument: Document) {
        val isNotPropertyAvailableTagsBeforeConvert = loadedDocument.getElementsByTagName("isNotPropertyAvailable")
        assertEquals(2, isNotPropertyAvailableTagsBeforeConvert.length)
        assertEquals("and (", attributeValue(loadedDocument, "isNotPropertyAvailable", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "isNotPropertyAvailable", "close", 0))
        assertEquals("name", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 0))
        assertEquals("\n      name = #name3#\n      \n    ", textContent(loadedDocument, "isNotPropertyAvailable", 0))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "prepend", 0))

        assertEquals("or", attributeValue(loadedDocument, "isNotPropertyAvailable", "prepend", 1))
        assertEquals("name2", attributeValue(loadedDocument, "isNotPropertyAvailable", "property", 1))
        assertEquals("\n        name = #name4#\n      ", textContent(loadedDocument, "isNotPropertyAvailable", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "open", 1))
        assertFalse(existsAttribute(loadedDocument, "isNotPropertyAvailable", "close", 1))
    }

    private fun isNotPropertyAvailableTagTestAfterConvert(convertedDocument: Document) {
        val ifTagsAfterConvert = convertedDocument.getElementsByTagName("if")
        assertEquals(COUNT_OF_IF_TAG, ifTagsAfterConvert.length)
        assertEquals("!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name')", attributeValue(convertedDocument, "if", "test", 22))
        assertEquals("\n       and ( name = #{name3}\n       ) \n    ", textContent(convertedDocument, "if", 22))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 22))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 22))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 22))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 22))

        assertEquals("!_parameter instanceof org.apache.ibatis.session.defaults.DefaultSqlSession\$StrictMap and _parameter instanceof java.util.Map and !_parameter.containsKey('name2')", attributeValue(convertedDocument, "if", "test", 23))
        assertEquals("\n         or name = #{name4}\n      ", textContent(convertedDocument, "if", 23))
        assertFalse(existsAttribute(convertedDocument, "if", "prepend", 23))
        assertFalse(existsAttribute(convertedDocument, "if", "property", 23))
        assertFalse(existsAttribute(convertedDocument, "if", "open", 23))
        assertFalse(existsAttribute(convertedDocument, "if", "close", 23))
    }

    private fun dynamicTagTestBeforeConvert(loadedDocument: Document) {
        val dynamicTagsBeforeConvert = loadedDocument.getElementsByTagName("dynamic")
        assertEquals(2, dynamicTagsBeforeConvert.length)
        assertEquals("where", attributeValue(loadedDocument, "dynamic", "prepend", 0))
        assertEquals("set", attributeValue(loadedDocument, "dynamic", "prepend", 1))
    }

    private fun dynamicTagTestAfterConvert(convertedDocument: Document) {
        val trimTagsAfterConvert = convertedDocument.getElementsByTagName("trim")
        assertEquals(2, trimTagsAfterConvert.length)
        assertEquals("where", attributeValue(convertedDocument, "trim", "prefix", 0))
        assertEquals("AND |OR ", attributeValue(convertedDocument, "trim", "prefixOverrides", 0))

        assertEquals("set", attributeValue(convertedDocument, "trim", "prefix", 1))
        assertEquals(", |AND |OR ", attributeValue(convertedDocument, "trim", "prefixOverrides", 1))

        assertFalse(existsAttribute(convertedDocument, "trim", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "trim", "open", 0))
        assertFalse(existsAttribute(convertedDocument, "trim", "close", 0))

        assertFalse(existsAttribute(convertedDocument, "trim", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "trim", "open", 1))
        assertFalse(existsAttribute(convertedDocument, "trim", "close", 1))
    }

    private fun iterateTagTestBeforeConvert(loadedDocument: Document) {
        val iterateTagsBeforeConvert = loadedDocument.getElementsByTagName("iterate")
        assertEquals(3, iterateTagsBeforeConvert.length)
        assertEquals("list", attributeValue(loadedDocument, "iterate", "property", 0))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 0))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 0))
        assertEquals(" OR ", attributeValue(loadedDocument, "iterate", "conjunction", 0))
        assertEquals("\n        (name = #list[].name#\n      \n        )\n    ", textContent(loadedDocument, "iterate", 0))

        assertEquals("list[].subList", attributeValue(loadedDocument, "iterate", "property", 1))
        assertEquals("id IN", attributeValue(loadedDocument, "iterate", "prepend", 1))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 1))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 1))
        assertEquals(",", attributeValue(loadedDocument, "iterate", "conjunction", 1))
        assertEquals("\n          #list[].subList[].id#\n      ", textContent(loadedDocument, "iterate", 1))

        assertEquals("AND", attributeValue(loadedDocument, "iterate", "prepend", 2))
        assertEquals("(", attributeValue(loadedDocument, "iterate", "open", 2))
        assertEquals(")", attributeValue(loadedDocument, "iterate", "close", 2))
        assertEquals(" AND ", attributeValue(loadedDocument, "iterate", "conjunction", 2))
        assertEquals("\n        id = #[]#\n      ", textContent(loadedDocument, "iterate", 2))
        assertFalse(existsAttribute(loadedDocument, "iterate", "property", 2))
    }

    private fun iterateTagTestAfterConvert(convertedDocument: Document) {
        val foreachTagsAfterConvert = convertedDocument.getElementsByTagName("foreach")
        assertEquals(3, foreachTagsAfterConvert.length)
        assertEquals("listItem", attributeValue(convertedDocument, "foreach", "item", 0))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 0))
        assertEquals("(", attributeValue(convertedDocument, "foreach", "open", 0))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 0))
        assertEquals(" OR ", attributeValue(convertedDocument, "foreach", "separator", 0))
        assertEquals("\n        (name = #{listItem.name}\n      \n        )\n    ", textContent(convertedDocument, "foreach", 0))

        assertEquals("listItem.subListItem", attributeValue(convertedDocument, "foreach", "item", 1))
        assertEquals("listItem.subList", attributeValue(convertedDocument, "foreach", "collection", 1))
        assertEquals("id IN (", attributeValue(convertedDocument, "foreach", "open", 1))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 1))
        assertEquals(",", attributeValue(convertedDocument, "foreach", "separator", 1))
        assertEquals("\n          #{listItem.subListItem.id}\n      ", textContent(convertedDocument, "foreach", 1))

        assertEquals("item", attributeValue(convertedDocument, "foreach", "item", 2))
        assertEquals("list", attributeValue(convertedDocument, "foreach", "collection", 2))
        assertEquals("AND (", attributeValue(convertedDocument, "foreach", "open", 2))
        assertEquals(")", attributeValue(convertedDocument, "foreach", "close", 2))
        assertEquals(" AND ", attributeValue(convertedDocument, "foreach", "separator", 2))
        assertEquals("\n        id = #{item}\n      ", textContent(convertedDocument, "foreach", 2))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 0))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 0))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 1))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 1))

        assertFalse(existsAttribute(convertedDocument, "foreach", "prepend", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "property", 2))
        assertFalse(existsAttribute(convertedDocument, "foreach", "conjunction", 2))
    }

    private fun loadValidDocument(): Document {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE sqlMap
              PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
              "http://ibatis.apache.org/dtd/sql-map-2.dtd">

            <sqlMap namespace="jp.ogasada.test">
              <resultMap id="findResult" class="HashMap" groupBy="id">
                <result property="id" column="id" javaType="int" />
                <result property="name" column="name" javaType="String" />
                <result property="detailList" javaType="List" resultMap="jp.ogasada.ibatistomybatis3.detailResult" />
              </resultMap>
              <select id="find" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                WHERE
                  id = #id#
                <isEmpty open="and (" property="name" close=")">
                  name = #name#
                  <isEmpty prepend="or" property="name2">
                    name = #name2#
                  </isEmpty>
                </isEmpty>
              </select>
              <select id="find2" resultClass="String" parameterClass="long">
                SELECT
                  name
                FROM
                  testTable
                WHERE
                <isNotEmpty open="and (" property="name" close=")">
                  name = #name#
                  <isNotEmpty prepend="or" property="name2">
                    name = #name2#
                  </isNotEmpty>
                </isNotEmpty>
                <isEqual open="and (" property="name" compareValue="foo" close=")">
                  name = #name#
                  <isEqual prepend="or" property="name2" compareProperty="compareName2">
                    name = #name2#
                  </isEqual>
                </isEqual>
                <isNotEqual open="and (" property="name" compareValue="foo" close=")">
                  name = #name#
                  <isNotEqual prepend="or" property="name2" compareProperty="compareName2">
                    name = #name2#
                  </isNotEqual>
                </isNotEqual>
                <isGreaterEqual open="and (" property="id" compareValue="1" close=")">
                  id = #id#
                  <isGreaterEqual prepend="or" property="id2" compareProperty="compareId2">
                    id = #id2#
                  </isGreaterEqual>
                </isGreaterEqual>
                <isGreaterThan open="and (" property="id" compareValue="1" close=")">
                  id = #id#
                  <isGreaterThan prepend="or" property="id2" compareProperty="compareId2">
                    id = #id2#
                  </isGreaterThan>
                </isGreaterThan>
                <isLessEqual open="and (" property="id" compareValue="1" close=")">
                  id = #id#
                  <isLessEqual prepend="or" property="id2" compareProperty="compareId2">
                    id = #id2#
                  </isLessEqual>
                </isLessEqual>
                <isLessThan open="and (" property="id" compareValue="1" close=")">
                  id = #id#
                  <isLessThan prepend="or" property="id2" compareProperty="compareId2">
                    id = #id2#
                  </isLessThan>
                </isLessThan>
                <isNull open="and (" property="name" close=")">
                  name = #name#
                  <isNull prepend="or" property="name2">
                    name = #name2#
                  </isNull>
                </isNull>
                <isNotNull open="and (" property="name" close=")">
                  name = #name#
                  <isNotNull prepend="or" property="name2">
                    name = #name2#
                  </isNotNull>
                </isNotNull>
                <isPropertyAvailable open="and (" property="name" close=")">
                  name = #name#
                  <isPropertyAvailable prepend="or" property="name2">
                    name = #name2#
                  </isPropertyAvailable>
                </isPropertyAvailable>
                <isNotPropertyAvailable open="and (" property="name" close=")">
                  name = #name3#
                  <isNotPropertyAvailable prepend="or" property="name2">
                    name = #name4#
                  </isNotPropertyAvailable>
                </isNotPropertyAvailable>
                <iterate property="list" open="(" close=")" conjunction=" OR " >
                    (name = #list[].name#
                  <iterate property="list[].subList" prepend="id IN" open="(" close=")" conjunction="," >
                      #list[].subList[].id#
                  </iterate>
                    )
                </iterate>
              </select>
              <select id="find3" resultMap="findResult" parameterClass="long">
                SELECT
                  id,
                  name
                FROM
                  testTable
                <dynamic prepend="where">
                  <isEqual prepend="and" property="id" compareValue="1">
                    id = #id#
                  </isEqual>
                  <isEqual prepend="and" property="name" compareValue="foo">
                    name = #name#
                  </isEqual>
                  <iterate prepend="AND" open="(" close=")" conjunction=" AND " >
                    id = #[]#
                  </iterate>
                </dynamic>
              </select>
              <insert id="insert" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                INSERT INTO testTable (
                  id,
                  name
                )
                VALUES (
                  #id#,
                  #name#
                )
              </insert>
              <update id="update" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                UPDATE testTable
                <dynamic prepend="set">
                  <isNotNull prepend="," property="id">
                    id = #id#
                  </isNotNull>
                  <isNotNull prepend="," property="name">
                    name = #name#
                  </isNotNull>
                </dynamic>
                WHERE
                  key = #key#
              </update>
              <delete id="delete" parameterClass="jp.ogasada.ibatistomybatis3.TestTableEntity" >
                DELETE FROM testTable
                WHERE
                  id = #id#
              </delete>
            </sqlMap>
            """.trimIndent()

        return loadXML(xml)
    }
}
