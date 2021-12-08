fun main() {
    //part1 simple solution based on length
    val result1 = day8SampleData().map { it.split("|").takeLast(1) }.flatten()
        .flatMap { it.split(" ") }.filter { it.length in arrayListOf(2, 4, 3, 7) }
    println(result1.count())
    check(result1.count() == 318)

    //part 1 recognize based on left side data
    val result1u = day8SampleData().map { decodeDigits(it) }
        .flatMap { it.split("") }.filter { it.isNotBlank() } .filter { it.toInt() in arrayListOf(1, 4, 7, 8) }
    println(result1u.count())
    check(result1u.count() == 318)

    //part2
    val result2 = day8SampleData().map { decodeDigits(it) }.sumOf { it.toInt() }
    println(result2)

}

fun decodeDigits(input: String): String {
    val data = input.split("|").map {
        it.trim()
    }
    val map = recognizeDigits(data[0])
    return translateInput(map, data[1])
}

private fun translateInput(map: Map<String, Int>, input: String): String {
    return input.split(" ").map { it.toCharArray().sorted().joinToString("") }
        .joinToString("") {
            map[it].toString()
        }
}

private fun recognizeDigits(s: String): Map<String, Int> {
    val map = mutableMapOf<Int, String>()
    val split = s.split(" ").map {
        it.toCharArray().sorted().joinToString("")
    }
    map[1] = split.first { it.length == 2 }
    map[4] = split.first { it.length == 4 }
    map[7] = split.first { it.length == 3 }
    map[8] = split.first { it.length == 7 }
    map[9] = split.first {
        it.length == 6 && it.toList().containsAll(map[1]!!.toList()) && it.toList().containsAll(map[4]!!.toList())
    }
    map[0] = split.first { it.length == 6 && it.toList().containsAll(map[7]!!.toList()) && it != map[9] }
    map[3] = split.first { it.length == 5 && it.toList().containsAll(map[7]!!.toList()) }
    map[5] =
        split.first { it.length == 5 && it.toList().filter { ch -> map[9]!!.contains(ch) }.size == 5 && it != map[3] }
    map[2] = split.first { it.length == 5 && it.toList().filter { ch -> map[9]!!.contains(ch) }.size == 4 }
    map[6] = split.first { it.length == 6 && it != map[9] && it != map[0] }
    return map.entries.associate { it.value to it.key }
}

fun day8SampleData() = readFileFromResources("day8_data")
fun sampleDataDay8() = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |fgae cfgab fg bagce""".lines().filter { it.isNotBlank() }
