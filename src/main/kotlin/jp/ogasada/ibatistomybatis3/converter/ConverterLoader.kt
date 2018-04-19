package jp.ogasada.ibatistomybatis3.converter

object ConverterLoader {

    private val converters = listOf(
            DynamicTagConverter,
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
            IsLessThanTagConverter,
            IsNullTagConverter,
            IsNotNullTagConverter,
            IsPropertyAvailableTagConverter,
            IsNotPropertyAvailableTagConverter)

    fun load(): List<ITagConverter> = converters
}
