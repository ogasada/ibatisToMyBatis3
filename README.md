# iBatisToMyBatis3

The tool converts sqlmap files for iBatis to MyBatis3.

## Prerequisites

### JDK

In order to use this tool you need to have `JDK1.8`~

## Usage

### 1. git clone this project

`$ git clone https://github.com/ogasada/ibatisToMyBatis3.git`

### 2. configure sqlmap file path for convert

change `systemProp.targetDir` in `gradle.properties` file

### 3. execute

`$ ./gradlew convert`

* create backup to `${systemProp.targetDir}`\_bak\_${currentTimeMillis}
* convert sqlmap file to mapper file (overwrite)

## Features

convert tags as follows

|before(tag)|before(attribute)|after(tag)|after(attribute)|
|:--|:--|:--|:--|
|sqlMap| |mapper| |
| |namespace| |namespace|
|resultMap| |resultMap| |
| |class| |type|
| |id| |id|
| |groupBy| |(none)|
|result| |result| |
| |property| |property|
| |column| |column|
| |javaType| |javaType|
|result| |collection (when contains resultMap attribute)| |
| |property| |property|
| |column| |column|
| |javaType| |javaType|
| |resultMap| |resultMap|
|select| |select| |
| |id| |id|
| |parameterClass| |parameterType|
| |resultClass| |resultType|
| |resultMap| |resultMap|
|insert| |insert| |
| |id| |id|
| |parameterClass| |parameterType|
|update| |update| |
| |id| |id|
| |parameterClass| |parameterType|
|delete| |delete| |
| |id| |id|
| |parameterClass| |parameterType|
|isEmpty| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isNotEmpty| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isEqual| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isNotEqual| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isGreaterEqual| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isGreaterThan| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isLessEqual| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isLessThan| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |compareValue| |test|
| |compareProperty| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isNull| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isNotNull| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isNotPropertyAvailable| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|isPropertyAvailable| |if| |
| |prepend| |(add to the begining of the text node)|
| |property| |test|
| |open| |(add to the begining of the text node)|
| |close| |(add to the end of the text node)|
|dynamic||trim||
| |prepend| |prefix|
| |close| |suffix|
|iterate| |foreach| |
| |prepend| |open|
| |property| |collection|
| |open| |open|
| |close| |close|
| |conjunction| |separator|

