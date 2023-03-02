package wordsvirtuoso

import java.io.File
import java.util.*
import kotlin.system.exitProcess

fun main(vararg args: String) {
    if (args.size != 2) {
        println("Error: Wrong number of arguments.")
        exitProcess(1)
    }

    val file = File(args[0])
    if (!file.exists()) {
        println("Error: The words file ${args[0]} doesn't exist.")
        exitProcess(1)
    }

    val file2 = File(args[1])
    if (!file2.exists()) {
        println("Error: The candidate words file ${args[1]} doesn't exist.")
        exitProcess(1)
    }

    val allWords = file.readLines().map { it.trim().lowercase(Locale.getDefault()) }

    val invalidWords = allWords.count { !checkString(it) }
    if (invalidWords > 0) {
        println("Error: $invalidWords invalid words were found in the ${args[0]} file.")
        exitProcess(1)
    }

    val candidateWords = file2.readLines().map { it.trim().lowercase(Locale.getDefault()) }
    val invalidWords2 = candidateWords.count { !checkString(it) }
    if (invalidWords2 > 0) {
        println("Error: $invalidWords2 invalid words were found in the ${args[1]} file.")
        exitProcess(1)
    }

    val notIncluded = candidateWords.count { !allWords.contains(it) }
    if (notIncluded > 0) {
        println("Error: $notIncluded candidate words are not included in the ${args[0]} file.")
        exitProcess(1)
    }

    println("Words Virtuoso")

    //Choose a secret word from the candidate words file.
    val secretWord = candidateWords.random()


    while (true) {
        println("Input a 5-letter word:")
        val input = readln().trim().lowercase(Locale.getDefault())

        if (input == "exit") {
            println("The game is over.")
            exitProcess(0)
        }

        if (input.length != 5) {
            println("The input isn't a 5-letter word.")
            continue
        }

        if (input.contains(Regex("[^a-zA-Z]"))) {
            println("One or more letters of the input aren't valid.")
            continue
        }

        if (input.toSet().size != input.length) {
            println("The input has duplicate letters.")
            continue
        }

        if (!allWords.contains(input)) {
            println("The input word isn't included in my words list.")
            continue
        }

        //If the input is the same as the secret word, print Correct! and exit.
        if (input == secretWord) {
            println("Correct!")
            exitProcess(0)
        }

        val clue = CharArray(input.length) { '_' }
        for (i in input.indices) {
            if (input[i] == secretWord[i]) {
                clue[i] = input[i].uppercaseChar()
            } else if (secretWord.contains(input[i])) {
                clue[i] = input[i].lowercaseChar()
            }
        }

        println(StringBuilder().apply { append(clue) }.toString())
        println()
    }
}

fun checkString(input: String): Boolean {
    return if (input.length != 5) {
        false
    } else if (input.contains(Regex("[^a-zA-Z]"))) {
        false
    } else input.toSet().size == input.length
}
