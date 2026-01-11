import cs1.trees.BinaryTree
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.Random

class TestBinaryTree :
    StringSpec({
        "it should create a single-node tree" {
            val tree = BinaryTree(42)
            tree.value shouldBe 42
            tree.left shouldBe null
            tree.right shouldBe null
        }
        "it should create a tree with left and right children" {
            val tree = BinaryTree(1, 2, 3)
            tree.value shouldBe 1
            tree.left?.value shouldBe 2
            tree.right?.value shouldBe 3
            tree.left?.left shouldBe null
            tree.left?.right shouldBe null
            tree.right?.left shouldBe null
            tree.right?.right shouldBe null
        }
        "it should create a tree with only left child" {
            val tree = BinaryTree(1, 2, null)
            tree.value shouldBe 1
            tree.left?.value shouldBe 2
            tree.right shouldBe null
        }
        "it should create a tree with only right child" {
            val tree = BinaryTree(1, null, 3)
            tree.value shouldBe 1
            tree.left shouldBe null
            tree.right?.value shouldBe 3
        }
        "it should copy a tree" {
            val original = BinaryTree(1, 2, 3)
            val copy = BinaryTree(original)
            copy shouldBe original
            (copy === original) shouldBe false
            copy.value shouldBe original.value
            copy.left shouldBe original.left
            copy.right shouldBe original.right
            (copy.left === original.left) shouldBe false
            (copy.right === original.right) shouldBe false
        }
        "it should test equality correctly" {
            val tree1 = BinaryTree(1, 2, 3)
            val tree2 = BinaryTree(1, 2, 3)
            val tree3 = BinaryTree(1, 2, 4)
            val tree4 = BinaryTree(1, 3, 2)

            tree1 shouldBe tree2
            tree1 shouldNotBe tree3
            tree1 shouldNotBe tree4
            tree1 shouldNotBe "not a tree"
            tree1 shouldNotBe null
        }
        "it should have consistent hashCode" {
            val tree1 = BinaryTree(1, 2, 3)
            val tree2 = BinaryTree(1, 2, 3)
            tree1.hashCode() shouldBe tree2.hashCode()
        }
        "it should have a meaningful toString" {
            val tree = BinaryTree(1, 2, 3)
            tree.toString() shouldBe "BinaryTree(3 nodes)"
        }
        "it should create a random integer tree" {
            val tree = BinaryTree.randomIntegerTree(10)
            countNodes(tree) shouldBe 10
        }
        "it should create a random integer tree with custom max" {
            val tree = BinaryTree.randomIntegerTree(10, 50)
            countNodes(tree) shouldBe 10
        }
        "it should create a random integer tree with seeded random" {
            val random = Random(124)
            val tree = BinaryTree.randomIntegerTree(random, 10, 128)
            countNodes(tree) shouldBe 10
        }
        "it should create a random balanced integer tree" {
            val tree = BinaryTree.randomBalancedIntegerTree(31, 128)
            countNodes(tree) shouldBe 31
            val depth = treeDepth(tree)
            depth shouldBeLessThanOrEqual 6 // For a balanced tree of 31 nodes, depth should be ~5
        }
        "it should create a random balanced integer tree with seeded random" {
            val random = Random(124)
            val tree = BinaryTree.randomBalancedIntegerTree(random, 15, 128)
            countNodes(tree) shouldBe 15
        }
        "it should create a random string tree" {
            val tree = BinaryTree.randomStringTree(10, 16)
            countNodes(tree) shouldBe 10
            tree.value.length shouldBeGreaterThan 0
            tree.value.length shouldBeLessThanOrEqual 16
        }
        "it should create a random string tree with seeded random" {
            val random = Random(124)
            val tree = BinaryTree.randomStringTree(random, 10, 16)
            countNodes(tree) shouldBe 10
        }
        "it should create a random balanced string tree" {
            val tree = BinaryTree.randomBalancedStringTree(15, 16)
            countNodes(tree) shouldBe 15
        }
        "it should create a random balanced string tree with seeded random" {
            val random = Random(124)
            val tree = BinaryTree.randomBalancedStringTree(random, 15, 16)
            countNodes(tree) shouldBe 15
        }
        "it should reject zero or negative size for random trees" {
            shouldThrow<IllegalArgumentException> {
                BinaryTree.randomIntegerTree(0)
            }
            shouldThrow<IllegalArgumentException> {
                BinaryTree.randomIntegerTree(-1)
            }
            shouldThrow<IllegalArgumentException> {
                BinaryTree.randomBalancedIntegerTree(0, 128)
            }
            shouldThrow<IllegalArgumentException> {
                BinaryTree.randomStringTree(0, 16)
            }
            shouldThrow<IllegalArgumentException> {
                BinaryTree.randomBalancedStringTree(0, 16)
            }
        }
        "it should generate unique random trees" {
            val trees = mutableSetOf<BinaryTree<Int>>()
            repeat(100) {
                trees += BinaryTree.randomIntegerTree(10)
            }
            trees.size shouldBe 100
        }
        "it should generate reproducible trees with same seed" {
            val tree1 = BinaryTree.randomIntegerTree(Random(42), 10, 128)
            val tree2 = BinaryTree.randomIntegerTree(Random(42), 10, 128)
            tree1 shouldBe tree2
        }
    })

private fun <T> countNodes(tree: BinaryTree<T>?): Int =
    if (tree == null) 0 else 1 + countNodes(tree.left) + countNodes(tree.right)

private fun <T> treeDepth(tree: BinaryTree<T>?): Int =
    if (tree == null) 0 else 1 + maxOf(treeDepth(tree.left), treeDepth(tree.right))
