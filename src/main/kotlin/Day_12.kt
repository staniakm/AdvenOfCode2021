fun main() {
    val data = readFileFromResources("day12_data")

    println("part1  " + part1(data))
    println("part2: " + part2(data))
}


class Passage(private val input: List<String>, val allowExtraCave: Boolean) {
    private val edges = mutableMapOf<String, Set<String>>()
        .withDefault { setOf() }.apply {
            input.map { it.split("-") }.forEach { (a, b) ->
                put(a, getValue(a) + b)
                put(b, getValue(b) + a)
            }
        }

    val allPaths = search("start", listOf())

    private fun search(curr: String, path: List<String>): List<List<String>> {
        val updatedPath = path + curr
        if (curr == "end") return listOf(updatedPath)
        return edges.getValue(curr)
            .filterNot { next ->
                next == "start" ||
                        next.isLower() && next in updatedPath &&
                        if (allowExtraCave) {
                            updatedPath.filter { it.isLower() }.groupingBy { it }.eachCount().values.any { it >= 2 }
                        } else true
            }.flatMap { search(it, updatedPath) }
    }
}

fun String.isLower() = this.all { it.isLowerCase() }

fun part1(input: List<String>) = Passage(input, allowExtraCave = false).allPaths.size

fun part2(input: List<String>) = Passage(input, allowExtraCave = true).allPaths.size