import cs125.sorting.quicksort.Partitioner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe

class TestPartitioner :
    StringSpec({
        "it should partition a simple array" {
            val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6)
            val pivotIndex = Partitioner.partition(array)
            val pivotValue = array[pivotIndex]

            // All elements before pivot should be less than pivot
            for (i in 0 until pivotIndex) {
                array[i] shouldBeLessThan pivotValue
            }
            // All elements after pivot should be >= pivot
            for (i in pivotIndex + 1 until array.size) {
                array[i] shouldBeGreaterThanOrEqual pivotValue
            }
        }
        "it should partition with start and end indices" {
            val array = arrayOf(5, 3, 8, 4, 2, 7, 1, 6)
            val pivotIndex = Partitioner.partition(array, 2, 6)
            val pivotValue = array[pivotIndex]

            // Elements in range [2, pivotIndex) should be less than pivot
            for (i in 2 until pivotIndex) {
                array[i] shouldBeLessThan pivotValue
            }
            // Elements in range (pivotIndex, 6) should be >= pivot
            for (i in pivotIndex + 1 until 6) {
                array[i] shouldBeGreaterThanOrEqual pivotValue
            }
        }
        "it should handle a single-element array" {
            val array = arrayOf(42)
            val pivotIndex = Partitioner.partition(array)
            pivotIndex shouldBe 0
            array[0] shouldBe 42
        }
        "it should handle a two-element sorted array" {
            val array = arrayOf(1, 2)
            val pivotIndex = Partitioner.partition(array)
            pivotIndex shouldBe 0
            array[0] shouldBe 1
            array[1] shouldBe 2
        }
        "it should handle a two-element reverse-sorted array" {
            val array = arrayOf(2, 1)
            val pivotIndex = Partitioner.partition(array)
            pivotIndex shouldBe 1
            array[0] shouldBe 1
            array[1] shouldBe 2
        }
        "it should handle an already sorted array" {
            val array = arrayOf(1, 2, 3, 4, 5)
            val pivotIndex = Partitioner.partition(array)
            pivotIndex shouldBe 0
            verifyPartition(array, pivotIndex)
        }
        "it should handle a reverse-sorted array" {
            val array = arrayOf(5, 4, 3, 2, 1)
            val pivotIndex = Partitioner.partition(array)
            pivotIndex shouldBe 4
            verifyPartition(array, pivotIndex)
        }
        "it should handle an array with duplicates" {
            val array = arrayOf(3, 1, 3, 1, 3, 1)
            val pivotIndex = Partitioner.partition(array)
            verifyPartition(array, pivotIndex)
        }
        "it should handle an array with all same elements" {
            val array = arrayOf(5, 5, 5, 5, 5)
            val pivotIndex = Partitioner.partition(array)
            array[pivotIndex] shouldBe 5
        }
        "it should work with strings" {
            val array = arrayOf("dog", "cat", "bird", "elephant", "ant")
            val pivotIndex = Partitioner.partition(array)
            verifyPartition(array, pivotIndex)
        }
        "it should reject null array" {
            shouldThrow<IllegalArgumentException> {
                Partitioner.partition<Int>(null, 0, 0)
            }
        }
        "it should reject empty array" {
            shouldThrow<IllegalArgumentException> {
                Partitioner.partition(emptyArray<Int>())
            }
        }
        "it should preserve all elements" {
            val original = arrayOf(3, 1, 4, 1, 5, 9, 2, 6)
            val array = original.copyOf()
            Partitioner.partition(array)

            // Verify all original elements are still present
            array.sorted() shouldBe original.sorted()
        }
    })

private fun <T : Comparable<T>> verifyPartition(array: Array<T>, pivotIndex: Int) {
    val pivotValue = array[pivotIndex]
    for (i in 0 until pivotIndex) {
        require(array[i] < pivotValue) { "Element at $i (${array[i]}) should be < pivot ($pivotValue)" }
    }
    for (i in pivotIndex + 1 until array.size) {
        require(array[i] >= pivotValue) { "Element at $i (${array[i]}) should be >= pivot ($pivotValue)" }
    }
}
