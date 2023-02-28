package wordsvirtuoso

fun main() {
    println("Input a 5-letter string:")
    var input = readln()

    if (input.length != 5) {
        println("The input isn't a 5-letter string.")
    } else if (input.contains(Regex("[^a-zA-Z]"))) {
        println("The input has invalid characters.")
    } else if (input.toSet().size != input.length) {
        println("The input has duplicate letters.")
    } else {
        println("The input is a valid string.")
    }



}
