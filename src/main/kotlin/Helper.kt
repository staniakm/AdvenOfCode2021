import java.io.File

fun readFileFromResources(fileName:String) = File("src/main/resources/$fileName").readText().lines().filter { it.isNotBlank() }

/**
 * Function copied from the internet
 */
fun convertBinaryToDecimal(inputNumber: Long): Int {
    var num = inputNumber
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}
