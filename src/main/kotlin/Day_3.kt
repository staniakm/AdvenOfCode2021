fun main() {
    val helper = calculateResult(day3SampleData())
    println(helper.calculateResult())//3923414

    // part 2
    //calculate oxygen generator rating
    val oxygenResult = calculateOxygen(day3SampleData())
    val oxygen = convertBinaryToDecimal(oxygenResult.toLong())
    println("output value list $oxygenResult $oxygen")

    //calculate oxygen generator rating
    val co2Result = calculateCo2(day3SampleData())
    val co2 = convertBinaryToDecimal(co2Result.toLong())
    println("output value list $co2Result $co2")

    val lifeSupportRating = oxygen * co2
    println(lifeSupportRating)
}

private fun calculateOxygen(inList: List<String>): String {
    val helper = calculateResult(inList)
    var index = 0
    var searchedChar = helper.getByIndex(0).returnMost()[0]
    var inputList = inList
    while (true) {
        inputList = getList(inputList, searchedChar, index)
        if (inputList.size != 1) {
            index++
            val h = calculateResult(inputList)
            searchedChar = h.getByIndex(index).returnMost()[0]
        } else {
            break
        }
    }
    return inputList[0]
}

private fun calculateCo2(inList: List<String>): String {
    val helper = calculateResult(inList)
    var index = 0
    var searchedChar = helper.getByIndex(0).returnLeast()[0]
    var inputList = inList
    while (true) {
        inputList = getList(inputList, searchedChar, index)
        if (inputList.size != 1) {
            index++
            val h = calculateResult(inputList)
            searchedChar = h.getByIndex(index).returnLeast()[0]

        } else {
            break
        }
    }
    return inputList[0]
}

fun calculateResult(sampleData: List<String>): HelperClass {
    val helper = HelperClass()

    sampleData.filter { it.isNotEmpty() }
        .map { word -> word.split("").filter { it.isNotBlank() } }
        .forEach {
            it.forEachIndexed { index, s -> helper.putValue(index, s) }
        }

    return helper
}

fun getList(list: List<String>, searchingValue: Char, index: Int): List<String> {
    return list.filter { it.isNotEmpty() }
        .filter { it[index] == searchingValue }
}

fun testSample() = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".lines()

data class CounterClass(val zeros: Int, val ones: Int) {
    fun addValue(value: String): CounterClass {
        return if (value == "1")
            this.copy(ones = ones + 1)
        else {
            this.copy(zeros = zeros + 1)
        }
    }

    fun returnMost(): String {
        return if (zeros > ones) {
            "0"
        } else {
            "1"
        }
    }

    fun returnLeast(): String {
        return if (zeros > ones) {
            "1"
        } else {
            "0"
        }
    }
}

class HelperClass() {
    private val map = mutableMapOf<Int, CounterClass>()

    fun putValue(index: Int, value: String) {
        map[index] = map.getOrDefault(index, CounterClass(0, 0)).addValue(value)
    }

    fun getByIndex(index: Int): CounterClass {
        return map[index]!!
    }

    fun calculateResult(): Int {
        var most = ""
        var least = ""
        map.forEach { (_, v) ->
            run {
                most += v.returnMost()
                least += v.returnLeast()
            }
        }
        val m = convertBinaryToDecimal(most.toLong())
        val l = convertBinaryToDecimal(least.toLong())
        return m * l
    }
}

fun day3SampleData() = readFileFromResources("day3_data")


