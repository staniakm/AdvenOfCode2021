fun main() {
    val (matrix: Array<Array<String>>, foldData) = prepareInputData()

    var processingMatrix = matrix

    //part1
    foldMatrix(processingMatrix, foldData[0])
        .sumOf { array ->
            array.count {
                it == "x"
            }
        }.let {
            println(it)
        }

    //part2
    repeat(foldData.size) {
        processingMatrix = foldMatrix(processingMatrix, foldData[it])
    }

    processingMatrix.forEach {
        println(it.contentToString())
    }
}

private fun foldMatrix(matrix: Array<Array<String>>, fold: Pair<String, Int>): Array<Array<String>> {
    return if (fold.first == "y") {
        (matrix.size - 1 downTo fold.second + 1)
            .map { toRep -> mergeArrays(matrix[matrix.size - 1 - toRep], matrix[toRep]) }
            .toTypedArray()
    } else {
        matrix.map { row ->
            val output = row.copyOfRange(0, fold.second)
            val newInput = row.copyOfRange(fold.second + 1, row.size).reversedArray()
            mergeArrays(output, newInput)
        }.toTypedArray()
    }

}

private fun prepareInputData(): Pair<Array<Array<String>>, List<Pair<String, Int>>> {
    val filter = sampleDataDay13().filter { it.contains(",") }
        .map { toCoordinates(it) }
    val maxX = filter.maxOf { it.first }
    val maxY = filter.maxOf { it.second }
    val matrix: Array<Array<String>> = drawMatrix(filter, maxX, maxY)

    val foldData = sampleDataDay13().filter { it.contains("fold along") }.map { toFold(it) }
    return Pair(matrix, foldData)
}

private fun mergeArrays(org: Array<String>, repl: Array<String>): Array<String> {
    return (org.indices)
        .map {
            if (repl[it] == "x") "x" else org[it]
        }.toTypedArray()
}

private fun toFold(it: String): Pair<String, Int> {
    val digit = it.filter { it.isDigit() }.toInt()
    val letter = it.filter { it == 'x' || it == 'y' }
    return Pair(letter, digit)
}

private fun drawMatrix(filter: List<Pair<Int, Int>>, maxX: Int, maxY: Int): Array<Array<String>> {
    return (0..maxY).map { y ->
        (0..maxX).map { x ->
            if (filter.contains(Pair(x, y))) "x" else "."
        }.toTypedArray()
    }.toTypedArray()

}

fun toCoordinates(it: String): Pair<Int, Int> {
    val split = it.split(",")
    return Pair(split[0].toInt(), split[1].toInt())
}

fun sampleDataDay13() = readFileFromResources("day13_data")

fun day13SampleData() = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".lines().filter { it.isNotBlank() }