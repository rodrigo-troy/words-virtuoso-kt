package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

fun main() {
    println("Input the words file:")
    val fileName = readln()

    val file = File(fileName)
    if (!file.exists()) {
        println("Error: The words file $fileName doesn't exist.")
        exitProcess(1)
    }

    val lines = file.readLines()
    val invalidWords = lines.count { !checkString(it) }

    if (invalidWords > 0) {
        println("Warning: $invalidWords invalid words were found in the $fileName file.")
        exitProcess(1)
    } else {
        println("All words are valid!")
    }
}

fun checkString(input: String): Boolean {
    return if (input.length != 5) {
        false
    } else if (input.contains(Regex("[^a-zA-Z]"))) {
        false
    } else input.toSet().size == input.length
}
