fun Array<Array<Boolean>>.getAt(x: Int, y: Int, unknown: Boolean): Boolean {
    return this.getOrNull(y)?.getOrNull(x) ?: unknown
}

fun Array<Array<Boolean>>.getSurrounding(x: Int, y: Int, unknown: Boolean, b: Boolean): Int {
    val replacement = if (b) unknown else false
    var result = 0

    // This looks stupid, but it's 4x faster than making a list and iterating over it
    if (this.getAt(x - 1, y - 1, replacement)) {
        result += 256
    }
    if (this.getAt(x, y - 1, replacement)) {
        result += 128
    }
    if (this.getAt(x + 1, y - 1, replacement)) {
        result += 64
    }
    if (this.getAt(x - 1, y, replacement)) {
        result += 32
    }
    if (this.getAt(x, y, replacement)) {
        result += 16
    }
    if (this.getAt(x + 1, y, replacement)) {
        result += 8
    }
    if (this.getAt(x - 1, y + 1, replacement)) {
        result += 4
    }
    if (this.getAt(x, y + 1, replacement)) {
        result += 2
    }
    if (this.getAt(x + 1, y + 1, replacement)) {
        result += 1
    }

    return result
}

fun main() {
    val input = readFileFromResources("day20_data")

    val d = Day20(input)

    check(d.part1() == 5097)
    check(d.part2() == 17987)
}

class Day20(inputData: List<String>) {
    private var map: Array<Array<Boolean>> = arrayOf()
    private var algorithm: Array<Boolean> = arrayOf()

    init {
        parseInput(inputData)
    }

    fun part1(): Int {
        repeat(2) {
            applyAlgorithm(it % 2 != 0)
        }

        val result = map.sumOf { row ->
            row.count { it }
        }

        return result
    }

    fun part2(): Int {
        repeat(48) {
            applyAlgorithm(it % 2 != 0)
        }

        val result = map.sumOf { row ->
            row.count { it }
        }

        return result
    }

    private fun parseInput(input: List<String>) {
        algorithm = input[0].map { it == '#' }.toTypedArray()

        map = input.drop(1).map { row ->
            row.map { it == '#' }.toList().toTypedArray()
        }.toTypedArray()
    }

    private fun applyAlgorithm(unknown: Boolean) {
        val maxY = map.size
        val maxX = map[0].size

        map = (-2..maxY + 1).map { y ->
            (-2..maxX + 1).map { x ->
                val surrounding = map.getSurrounding(x, y, unknown, algorithm[0])
                algorithm[surrounding]
            }.toTypedArray()
        }.toTypedArray()
    }
}