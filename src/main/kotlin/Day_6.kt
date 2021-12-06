import kotlin.collections.Map

fun main() {
    val initialData: List<Int> = day6SampleData().flatMap { data ->
        data.split(",").map { it.toInt() }
    }
    val map: Map<Int, Int> = initialData.groupingBy { it }.eachCount()
    println(simulateColony(256, map))

}

data class Colony(
    val age0: Long,
    val age1: Long,
    val age2: Long,
    val age3: Long,
    val age4: Long,
    val age5: Long,
    val age6: Long,
    val age7: Long,
    val age8: Long
) {
    fun sum(): Long {
        return (age0 + age1 + age2 + age3 + age4 + age5 + age6 + age7 + age8).toLong()
    }

    fun nextDay(): Colony {
        return this.copy(
            age0 = this.age1,
            age1 = this.age2,
            age2 = this.age3,
            age3 = this.age4,
            age4 = this.age5,
            age5 = this.age6,
            age6 = this.age7 + this.age0,
            age7 = this.age8,
            age8 = this.age0
        )
    }
}

fun getOrDefaultZero(map: Map<Int, Int>, index: Int): Long {
    return map.getOrDefault(index, 0).toLong()
}

private fun simulateColony(times: Int, ages: Map<Int, Int>): Long {
    var colony = Colony(
        getOrDefaultZero(ages, 0),
        getOrDefaultZero(ages, 1),
        getOrDefaultZero(ages, 2),
        getOrDefaultZero(ages, 3),
        getOrDefaultZero(ages, 4),
        getOrDefaultZero(ages, 5),
        getOrDefaultZero(ages, 6),
        getOrDefaultZero(ages, 7),
        getOrDefaultZero(ages, 8)
    )

    repeat(times) {
        colony = colony.nextDay()
    }
    return colony.sum()
}

fun sampleData6() = """3,4,3,1,2""".lines().filter { it.isNotBlank() }
fun day6SampleData() = readFileFromResources("day6_data")
