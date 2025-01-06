package cs125.trees

import java.util.Objects
import java.util.Random

@Suppress("unused")
class BinaryTree<T>(var value: T) {
    var left: BinaryTree<T>? = null
    var right: BinaryTree<T>? = null
    private var size: Int = 1

    constructor(value: T, setLeft: T?, setRight: T?) : this(value) {
        if (setLeft != null) {
            left = BinaryTree(setLeft)
            size++
        }
        if (setRight != null) {
            right = BinaryTree(setRight)
            size++
        }
    }

    constructor(other: BinaryTree<T>) : this(other.value) {
        copy(other, this)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other?.javaClass != javaClass -> false
            else -> {
                other as BinaryTree<*>
                return other.value == value && Objects.equals(other.left, left) && Objects.equals(other.right, right)
            }
        }
    }

    override fun hashCode() = Objects.hash(value, left, right)

    override fun toString() = "BinaryTree($size nodes)"

    companion object {
        private fun <T> copy(from: BinaryTree<T>, to: BinaryTree<T>) {
            require(from.value == to.value)
            if (from.left != null) {
                to.left = BinaryTree(from.left!!.value)
                copy(from.left!!, to.left!!)
            }
            if (from.right != null) {
                to.right = BinaryTree(from.right!!.value)
                copy(from.right!!, to.right!!)
            }
        }

        private fun <T> add(tree: BinaryTree<T>, value: T, random: Random) {
            if (random.nextBoolean()) {
                if (tree.right == null) {
                    tree.right = BinaryTree(value)
                } else {
                    add(tree.right!!, value, random)
                }
            } else {
                if (tree.left == null) {
                    tree.left = BinaryTree(value)
                } else {
                    add(tree.left!!, value, random)
                }
            }
            tree.size++
        }

        private fun <T> depth(tree: BinaryTree<T>?): Int = when (tree) {
            null -> 0
            else -> 1 + depth(tree.right).coerceAtLeast(depth(tree.left))
        }

        private fun <T> balancedAdd(tree: BinaryTree<T>, value: T, random: Random) {
            if (tree.right == null && tree.left == null) {
                if (random.nextBoolean()) {
                    tree.right = BinaryTree(value)
                } else {
                    tree.left = BinaryTree(value)
                }
            } else if (tree.right == null) {
                tree.right = BinaryTree(value)
            } else if (tree.left == null) {
                tree.left = BinaryTree(value)
            } else {
                val rightDepth = depth(tree.right)
                val leftDepth = depth(tree.left)
                if (leftDepth > rightDepth) {
                    balancedAdd(tree.right!!, value, random)
                } else if (rightDepth > leftDepth) {
                    balancedAdd(tree.left!!, value, random)
                } else {
                    if (random.nextBoolean()) {
                        balancedAdd(tree.right!!, value, random)
                    } else {
                        balancedAdd(tree.left!!, value, random)
                    }
                }
            }
            tree.size++
        }

        @JvmStatic
        fun randomIntegerTree(random: Random, size: Int, maxInteger: Int): BinaryTree<Int> {
            require(size > 0) { "size must be positive: $size" }
            return BinaryTree(random.nextInt(maxInteger) - (maxInteger / 2)).also { tree ->
                repeat(size - 1) {
                    add(tree, random.nextInt(maxInteger) - (maxInteger / 2), random)
                }
                check(tree.size == size)
            }
        }

        @JvmStatic
        fun randomIntegerTree(size: Int, maxInteger: Int): BinaryTree<Int> =
            randomIntegerTree(Random(), size, maxInteger)

        @JvmStatic
        fun randomIntegerTree(size: Int): BinaryTree<Int> = randomIntegerTree(Random(), size, 128)

        @JvmStatic
        fun randomBalancedIntegerTree(random: Random, size: Int, maxInteger: Int): BinaryTree<Int> {
            require(size > 0) { "size must be positive: $size" }
            return BinaryTree(random.nextInt(maxInteger / 2) - maxInteger).also { tree ->
                repeat(size - 1) {
                    balancedAdd(tree, random.nextInt(maxInteger / 2) - maxInteger, random)
                }
                check(tree.size == size)
            }
        }

        @JvmStatic
        fun randomBalancedIntegerTree(size: Int, maxInteger: Int): BinaryTree<Int> =
            randomBalancedIntegerTree(Random().apply { setSeed(124) }, size, maxInteger)

        private fun Random.nextIntRange(min: Int, max: Int) = let {
            require(min < max)
            nextInt(max - min) + min
        }

        @Suppress("SpellCheckingInspection")
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "

        private fun randomAlphanumericString(random: Random, maxLength: Int) =
            String(CharArray(random.nextInt(1, maxLength)) { CHARACTERS[random.nextInt(CHARACTERS.length)] })

        @JvmStatic
        fun randomStringTree(random: Random, size: Int, maxLength: Int): BinaryTree<String> {
            require(size > 0) { "size must be positive: $size" }
            return BinaryTree(randomAlphanumericString(random, maxLength)).also { tree ->
                repeat(size - 1) {
                    add(tree, randomAlphanumericString(random, maxLength), random)
                }
                check(tree.size == size)
            }
        }

        @JvmStatic
        fun randomStringTree(size: Int, maxLength: Int): BinaryTree<String> =
            randomStringTree(Random().apply { setSeed(124) }, size, maxLength)

        @JvmStatic
        fun randomBalancedStringTree(random: Random, size: Int, maxLength: Int): BinaryTree<String> {
            require(size > 0) { "size must be positive: $size" }
            return BinaryTree(randomAlphanumericString(random, maxLength)).also { tree ->
                repeat(size - 1) {
                    balancedAdd(tree, randomAlphanumericString(random, maxLength), random)
                }
                check(tree.size == size)
            }
        }

        @JvmStatic
        fun randomBalancedStringTree(size: Int, maxInteger: Int): BinaryTree<String> =
            randomBalancedStringTree(Random().apply { setSeed(124) }, size, maxInteger)
    }
}
