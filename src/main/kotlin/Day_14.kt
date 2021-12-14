fun main() {

    val inputData: String = sampleDataDay14().first()
    val dictionary: Map<String, String> = createDictionary(sampleDataDay14().drop(1))

    var windowed: MutableMap<String, Long> = mutableMapOf()
    inputData.windowed(2, 1)
        .forEach {
            windowed[it] = windowed.getOrDefault(it, 0L) + 1
        }

    repeat(40) {
        val tmp = mutableMapOf<String, Long>()
        windowed.map { (k, v) ->
            val newVal = dictionary[k]
            val s = k.split("").filterNot { it.isBlank() }
            tmp[s.first() + newVal] = tmp.getOrDefault(s.first() + newVal, 0L) + v
            tmp[newVal + s.last()] = tmp.getOrDefault(newVal + s.last(), 0L) + v
        }
        windowed = tmp
    }

    val result = mutableMapOf<String, Long>(inputData.last().toString() to 1)

    for ((k, v) in windowed) {
        val key = k.split("").filter { it.isNotBlank() }.first()
        result[key] = result.getOrDefault(key, 0) + v
    }
    println(result)
    val min = result.minOf { it.value }
    val max = result.maxOf { it.value }

    println("result = ${max - min}")

}

fun createDictionary(drop: List<String>): Map<String, String> {
    return drop.associate { line ->
        line.split("->").let {
            it[0].trim() to it[1].trim()
        }
    }
}

fun sampleData14() = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".lines().filter { it.isNotBlank() }

fun sampleDataDay14() = readFileFromResources("day14_data")
