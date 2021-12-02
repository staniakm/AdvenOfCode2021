fun main() {
    val result1 = day1sampleData().map { it.toInt() }.sumup()
    println(result1)

    val result2 = day1sampleData().map { it.toInt() }
        .windowed(3, 1)
        .map { it.sum() }
        .sumup()
    println(result2)
}

fun List<Int>.sumup() = this.windowed(2, 1)
    .fold(0) { acc: Int, ints: List<Int> -> acc + if (ints.component2() > ints.component1()) 1 else 0 }

fun day1sampleData() = readFileFromResources("day1_data")
