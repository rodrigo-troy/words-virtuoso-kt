package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

fun main(vararg args: String) {
    /*
    *  Check the number of the passed arguments. If it isn't equal to two, then print the message Error: Wrong number of arguments. and exit;
The second argument is the filename of the text file containing the candidate words. If this file doesn't exist, print the message Error: The candidate words file <Candidates filename> doesn't exist. and exit;
    * */
    if (args.size != 2) {
        println("Error: Wrong number of arguments.")
        exitProcess(1)
    }

    //The first argument is the filename of the text file containing all words. If this file doesn't exist, print the message Error: The words file <All words filename> doesn't exist. and exit;
    val file = File(args[0])
    if (!file.exists()) {
        println("Error: The words file ${args[0]} doesn't exist.")
        exitProcess(1)
    }

    //The second argument is the filename of the text file containing the candidate words. If this file doesn't exist, print the message Error: The candidate words file <Candidates filename> doesn't exist. and exit;
    val file2 = File(args[1])
    if (!file2.exists()) {
        println("Error: The candidate words file ${args[1]} doesn't exist.")
        exitProcess(1)
    }

    //Check the file containing all words for invalid words. If present, print Error: <Number of invalid words> invalid words were found in the <All words filename> file. and exit;
    val lines = file.readLines()
    val invalidWords = lines.count { !checkString(it) }
    if (invalidWords > 0) {
        println("Error: $invalidWords invalid words were found in the ${args[0]} file.")
        exitProcess(1)
    }

    //Check the file containing the candidate words for invalid words. If present, print Error: <Number of invalid words> invalid words were found in the <Candidates filename> file. and exit;
    val lines2 = file2.readLines()
    val invalidWords2 = lines2.count { !checkString(it) }
    if (invalidWords2 > 0) {
        println("Error: $invalidWords2 invalid words were found in the ${args[1]} file.")
        exitProcess(1)
    }

    //Check whether each candidate word is included in the file with all words. If any candidate words don't comply with that, print Error: <Number of candidates not included> candidate words are not included in the <All words filename> file. and exit;
    val notIncluded = lines2.count { !lines.contains(it.trim()) }
    if (notIncluded > 0) {
        println("Error: $notIncluded candidate words are not included in the ${args[0]} file.")
        exitProcess(1)
    }

    println("Words Virtuoso")
}

fun checkString(input: String): Boolean {
    return if (input.length != 5) {
        false
    } else if (input.contains(Regex("[^a-zA-Z]"))) {
        false
    } else input.toSet().size == input.length
}
