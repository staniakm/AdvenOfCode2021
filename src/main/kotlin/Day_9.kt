fun main() {

    HelperClass().recognize(day9SampleData())
}


data class FieldsHolder(val value: Int, val adjusted: List<Int>) {
    fun isLowest(): Boolean {
        return adjusted.filter { it > value }.size == adjusted.size
    }
}

class HelperClass {

    private val dataMap = mutableMapOf<Int, List<Int>>()

    fun recognize(input: List<String>) {

        input.forEachIndexed { index, s -> dataMap[index] = s.split("").filter { it.isNotBlank() }.map { it.toInt() } }

        val map = dataMap.entries
            .flatMap { e -> mapToLowerNumber(e) }
            .sumOf { it + 1 }

        println(map)
    }

    private fun mapToLowerNumber(entry: MutableMap.MutableEntry<Int, List<Int>>): List<Int> {
        return entry.value.mapIndexed { idx, value -> getAdjusted(entry.key, idx, value) }
            .filter {
                it.isLowest()
            }.map { it.value }
    }

    private fun getAdjusted(key: Int, idx: Int, value: Int): FieldsHolder {
        return FieldsHolder(
            value, listOf(
                dataMap.getOrDefault(key - 1, listOf()).getOrElse(idx) { -1 },
                dataMap.getOrDefault(key + 1, listOf()).getOrElse(idx) { -1 },
                dataMap[key]!!.getOrElse(idx - 1) { -1 },
                dataMap[key]!!.getOrElse(idx + 1) { -1 },
            ).filter { it != -1 }
        )
    }
}


fun day9SampleData() = readFileFromResources("day9_data")