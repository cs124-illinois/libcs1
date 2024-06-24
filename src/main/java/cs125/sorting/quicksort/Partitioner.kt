@file:Suppress("unused")

package cs125.sorting.quicksort

@Suppress("MemberVisibilityCanBePrivate", "UtilityClassWithPublicConstructor")
class Partitioner {
    companion object {
        @JvmStatic
        fun <T : Comparable<T>?> partition(
            values: Array<T>?,
            start: Int,
            end: Int,
        ): Int {
            require(values != null && values.isNotEmpty())
            var tmp: T
            var pivotPosition = start
            for (i in start + 1 until end) {
                if (values[i]!! < values[start]) {
                    pivotPosition++
                    tmp = values[pivotPosition]
                    values[pivotPosition] = values[i]
                    values[i] = tmp
                }
            }
            tmp = values[pivotPosition]
            values[pivotPosition] = values[start]
            values[start] = tmp
            return pivotPosition
        }

        @JvmStatic
        fun <T : Comparable<T>?> partition(values: Array<T>): Int = partition(values, 0, values.size)
    }
}
