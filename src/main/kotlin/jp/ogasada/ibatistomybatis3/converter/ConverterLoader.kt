package jp.ogasada.ibatistomybatis3.converter

object ConverterLoader {

    private val converters = listOf(SqlMapTagConverter)

    fun load(): List<SqlMapTagConverter> = converters
}
