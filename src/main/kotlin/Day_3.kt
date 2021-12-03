fun main() {
    val helper = HelperClass()

    day3SampleData().filter { it.isNotEmpty() }
        .map { word -> word.split("").filter { it.isNotBlank() } }
        .forEach {
            it.forEachIndexed { index, s -> helper.putValue(index, s) }
        }

    helper.calculateResult();
}

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

    fun calculateResult() {
        println(map)
        var most = ""
        var least = ""
        map.forEach { (_, v) ->
            run {
                most += v.returnMost()
                least += v.returnLeast()
            }
        }
        println("$most $least")
        val m = convertBinaryToDecimal(most.toLong())
        val l = convertBinaryToDecimal(least.toLong())
        println("$m  $l ")
        println(m * l)
    }
}


fun day3SampleData() = readFileFromResources("day3_data")


