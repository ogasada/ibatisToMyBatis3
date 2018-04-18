package jp.ogasada.ibatistomybatis3.converter

object ConverterLoader {

    private val converters = listOf(
            SqlMapTagConverter,
            ResultMapTagConverter,
            ResultTagConverter,
            SelectTagConverter,
            InsertTagConverter,
            UpdateTagConverter,
            DeleteTagConverter,
            IsEmptyTagConverter,
            IsNotEmptyTagConverter,
            IsEqualTagConverter,
            IsNotEqualTagConverter,
            IsGreaterEqualTagConverter,
            IsGreaterThanTagConverter,
            IsLessEqualTagConverter,
            IsLessThanTagConverter)

    fun load(): List<ITagConverter> = converters
}
