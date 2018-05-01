package jp.ogasada.ibatistomybatis3.converter

import org.w3c.dom.Document
import java.io.File
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.system.measureTimeMillis

const val PUBLIC_ID = "-//mybatis.org//DTD Mapper 3.0//EN"
const val SYSTEM_ID = "http://mybatis.org/dtd/mybatis-3-mapper.dtd"

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val measureTimeMillis = measureTimeMillis {
            if (args.isEmpty()) {
                throw IllegalArgumentException("need target directory")
            }

            val targetDirectory = File(args[0])
            if (targetDirectory.exists()) {
                createBackup(targetDirectory)
                convertSqlFiles(targetDirectory)
            } else {
                throw IllegalArgumentException("target directory (${targetDirectory}) is not exists")
            }
        }
        println("time: $measureTimeMillis ms")
    }

    private fun convertSqlFiles(directory: File) {
        val sqlFiles = directory.walkTopDown().filter { it.isFile && it.extension == "xml" }
        val count = sqlFiles.count()
        println("start conversion (${directory.path})")
        sqlFiles.forEachIndexed { index, file ->
            convertSqlFile(file)
            println("done: ${index + 1} / $count")
        }
        println("finish conversion (${directory.path})")
    }

    private fun convertSqlFile(sqlFile: File) {
        val loadedDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sqlFile)
        val convertedDocument: Document = SqlMapFileConverter.convert(loadedDocument)
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, PUBLIC_ID)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, SYSTEM_ID)
        FileOutputStream(sqlFile).use {
            val streamResult = StreamResult(it)
            transformer.transform(DOMSource(convertedDocument), streamResult)
        }
    }

    private fun createBackup(directory: File) {
        val backupDirectory = File(directory.parentFile, "${directory.name}_bak_${System.currentTimeMillis()}")
        println("start create backup (${directory.path} to ${backupDirectory.path})")
        directory.copyRecursively(backupDirectory)
        println("finish create backup (${directory.path} to ${backupDirectory.path})")
    }
}
