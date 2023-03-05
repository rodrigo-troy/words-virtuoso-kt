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

    val secretWord = candidateWords.random()
    var turns = 1
    var start = System.currentTimeMillis()
    var previousClues = mutableListOf<String>()
    var wrongChars = mutableSetOf<Char>()

    while (true) {
        println("Input a 5-letter word:")
        val input = readln().trim().lowercase(Locale.getDefault())

        if (input == secretWord && turns == 1) {
            println()
            println(input.uppercase(Locale.getDefault()))
            println()
            println("Correct!")
            println("Amazing luck! The solution was found at once.")
            exitProcess(0)
        }

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

        if (input == secretWord) {
            previousClues.add(input)
            printClues(previousClues)
            println("Correct!")
            break
        }

        val clue = getClue(input,
                           secretWord)

        val lastClue = StringBuilder().apply { append(clue) }.toString()
        previousClues.add(lastClue)
        printClues(previousClues)

        printWrongChars(input,
                        secretWord,
                        wrongChars)
        println()

        turns++
    }

    val end = System.currentTimeMillis()
    val duration = end - start

    println("The solution was found after $turns tries in ${duration / 1000} seconds.")
}

private fun printWrongChars(input: String,
                            secretWord: String,
                            wrongChars: MutableSet<Char>) {
    for (i in input.indices) {
        if (!secretWord.contains(input[i]) && !wrongChars.contains(input[i])) {
            wrongChars.add(input[i])
        }
    }

    println("\u001B[48:5:14m${wrongChars.sorted().joinToString("") { it.uppercaseChar().toString() }}\u001B[0m")
}

private fun getClue(input: String,
                    secretWord: String): CharArray {
    val clue = CharArray(input.length) { '_' }

    for (i in input.indices) {
        if (input[i] == secretWord[i]) {
            clue[i] = input[i].uppercaseChar()
        } else if (secretWord.contains(input[i])) {
            clue[i] = input[i].lowercaseChar()
        }
    }

    return clue
}

private fun printClues(previousClues: MutableList<String>) {
    println()
    for (clue in previousClues.reversed()) {
        for (i in clue.indices) {
            if (clue[i].isUpperCase()) {
                print("\u001B[48:5:10m${clue[i]}\u001B[0m")
            } else if (clue[i].isLowerCase()) {
                print("\u001B[48:5:11m${clue[i]}\u001B[0m")
            } else {
                print("\u001B[48:5:7m${clue[i]}\u001B[0m")
            }
        }
    }
    println()
}

fun checkString(input: String): Boolean {
    return if (input.length != 5) {
        false
    } else if (input.contains(Regex("[^a-zA-Z]"))) {
        false
    } else input.toSet().size == input.length
}
