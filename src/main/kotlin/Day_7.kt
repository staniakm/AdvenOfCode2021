import java.lang.Math.abs

fun main() {
    val data: List<Int> = day7SampleData().map { input -> input.split(",").map { it.toInt() } }.flatten()
    val min = data.minOf { it }
    val max = data.maxOf { it }
    val result = (min..max)
        .map { calculateMoveCost(data, it) }
        .minOf { it }
    println(result)
    check(result == 351901)
//part 2
    val result2 = (min..max)
        .map { calculateIncreaseMoveCost(data, it) }
        .minOf { it }
    println(result2)
    check(result2 == 101079875)


}

fun calculateMoveCost(data: List<Int>, n: Int): Int {
    return data.sumOf { value -> kotlin.math.abs(value - n)}
}

fun calculateIncreaseMoveCost(data: List<Int>, n: Int): Int {
    return data.sumOf { value -> kotlin.math.abs(value - n) * (kotlin.math.abs(value - n) + 1) / 2}
}


fun sampleData7() = """16,1,2,0,4,2,7,1,2,14""".lines().filter { it.isNotBlank() }
fun day7SampleData() = readFileFromResources("day7_data")
