package jp.ogasada.ibatistomybatis3.converter

object ConverterLoader {

    private val converters = listOf(
            SqlMapTagConverter,
            ResultMapTagConverter,
            ResultTagConverter,
            SelectTagConverter,
            InsertTagConverter,
            UpdateTagConverter,
            DeleteTagConverter)

    fun load(): List<ITagConverter> = converters
}
