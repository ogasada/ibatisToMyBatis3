package jp.ogasada.ibatistomybatis3.converter

object ConverterLoader {

    private val converters = listOf(
            DynamicTagConverter,
            IterateTagConverter,
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
            IsNotPropertyAvailableTagConverter,
            ParameterConverter)

    fun load(): List<ITagConverter> = converters
}
