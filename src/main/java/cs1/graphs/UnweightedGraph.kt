package cs1.graphs

import java.util.Objects
import java.util.Random

class Node<T>(var value: T, val nonce: Int) {
    override fun equals(other: Any?) = when (other) {
        !is Node<*> -> false
        else -> value == other.value && nonce == other.nonce
    }

    override fun hashCode() = Objects.hash(value, nonce)
}

class GraphNode<T> @JvmOverloads constructor(val value: T, val nonce: Int, var neighbors: Set<GraphNode<T>> = setOf()) {
    constructor(node: GraphNode<T>) : this(node.value, node.nonce)

    override fun equals(other: Any?) = when (other) {
        !is GraphNode<*> -> false
        else -> value == other.value && nonce == other.nonce
    }

    override fun hashCode() = Objects.hash(value, nonce)
}

private fun <T> GraphNode<T>.find(): Set<GraphNode<T>> = mutableSetOf<GraphNode<T>>().also { nodes ->
    find(nodes)
}

private fun <T> GraphNode<T>.find(visited: MutableSet<GraphNode<T>>) {
    visited += this
    for (neighbor in neighbors) {
        if (neighbor !in visited) {
            neighbor.find(visited)
        }
    }
}

@Suppress("NestedBlockDepth")
fun <T> Map<Node<T>, Set<Node<T>>>.toGraphNodes(random: Random) =
    keys.associateWith { GraphNode(it.value, random.nextInt()) }.let { mapping ->
        map { (key, values) ->
            check(mapping[key] != null) { "Missing mapping for node in graph creation" }
            mapping[key]!! to values.map { value ->
                check(mapping[value] != null) { "Missing mapping for node in graph creation" }
                mapping[value]!!
            }.toSet()
        }.toMap().apply {
            forEach { (node, neighbors) ->
                node.neighbors = neighbors
            }
            check(keys.first().find() == mapping.values.toSet()) { "Graph is not connected" }
            keys.forEach { node ->
                check(node !in node.neighbors) { "Graph contains a self-edge" }
                for (neighbor in node.neighbors) {
                    check(node in neighbor.neighbors) { "Graph is not undirected" }
                }
            }
        }
    }

private fun <T> Map<GraphNode<T>, Set<GraphNode<T>>>.copyGraphNodes() =
    keys.associateWith { GraphNode(it) }.let { mapping ->
        map { (key, values) ->
            check(mapping[key] != null) { "Missing mapping for node in graph copy" }
            mapping[key]!! to values.map { value ->
                check(mapping[value] != null) { "Missing mapping for node in graph creation" }
                mapping[value]!!
            }.toSet()
        }.toMap().onEach { (node, neighbors) ->
            node.neighbors = neighbors
        }
    }

fun Map<GraphNode<*>, Set<GraphNode<*>>>.toNodes() = keys.associateWith { Node(it.value, 0) }.let { mapping ->
    map { (key, values) ->
        check(mapping[key] != null) { "Missing mapping for node in graph creation" }
        mapping[key]!! to values.map { value ->
            check(mapping[value] != null) { "Missing mapping for node in graph creation" }
            mapping[value]!!
        }.toSet()
    }.toMap()
}

@Suppress("unused")
class UnweightedGraph<T> private constructor(
    val edges: Map<GraphNode<T>, Set<GraphNode<T>>>,
    @Suppress("unused") private val unused: Boolean
) {
    constructor(edges: Map<Node<T>, Set<Node<T>>>, random: Random = Random()) : this(edges.toGraphNodes(random), true)
    constructor(graph: UnweightedGraph<T>) : this(graph.edges.copyGraphNodes(), true)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) = when (other) {
        !is UnweightedGraph<*> -> false
        else -> (edges as Map<GraphNode<*>, Set<GraphNode<*>>>).toNodes() ==
            (other.edges as Map<GraphNode<*>, Set<GraphNode<*>>>).toNodes()
    }

    override fun hashCode() = Objects.hash(edges)

    val node = edges.keys.minByOrNull { it.nonce }!!

    @Suppress("TooManyFunctions")
    companion object {
        @JvmStatic
        fun <T> singleNodeGraph(value: T, random: Random = Random()) =
            UnweightedGraph(mapOf(Node(value, 0) to setOf()), random)

        @JvmStatic
        fun <T> twoNodeGraph(first: T, second: T, random: Random = Random()): UnweightedGraph<T> {
            val mapping = mapOf(0 to Node(first, 0), 1 to Node(second, 1))
            return UnweightedGraph(
                mapOf(
                    mapping[0]!! to setOf(mapping[1]!!),
                    mapping[1]!! to setOf(mapping[0]!!)
                ),
                random
            )
        }

        @JvmStatic
        fun <T> circleGraph(list: List<T>, random: Random = Random()): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, it -> i to Node(it, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }
            edges[mapping[0]]!! += mapping[list.size - 1]!!
            edges[mapping[list.size - 1]]!! += mapping[0]!!
            return UnweightedGraph(edges, random)
        }

        @JvmStatic
        fun <T> fullyConnectedGraph(list: List<T>, random: Random = Random()): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, it -> i to Node(it, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                for (j in (i + 1) until list.size) {
                    edges[mapping[i]]!! += mapping[j]!!
                    edges[mapping[j]]!! += mapping[i]!!
                }
            }
            return UnweightedGraph(edges, random)
        }

        @JvmStatic
        fun <T> randomGraph(list: List<T>, random: Random = Random()): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, it -> i to Node(it, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                val previous = ((i + 1) until list.size).toList()
                if (previous.isEmpty()) {
                    continue
                }
                for (j in previous.shuffled(random).take(random.nextInt(previous.size) + 1)) {
                    edges[mapping[i]]!! += mapping[j]!!
                    edges[mapping[j]]!! += mapping[i]!!
                }
            }
            return UnweightedGraph(edges, random)
        }

        @JvmStatic
        fun randomIntegerGraph(random: Random, size: Int, maxInteger: Int): UnweightedGraph<Int> {
            require(size > 0) { "size must be positive: $size" }
            return randomGraph(List(size) { random.nextInt(maxInteger) - (maxInteger / 2) }, random)
        }

        @JvmStatic
        fun randomIntegerGraph(size: Int, maxInteger: Int): UnweightedGraph<Int> {
            return randomIntegerGraph(Random(), size, maxInteger)
        }

        @JvmStatic
        fun randomIntegerGraph(size: Int): UnweightedGraph<Int> {
            return randomIntegerGraph(Random(), size, 128)
        }

        private fun Random.nextInt(min: Int, max: Int) = let {
            require(min < max)
            nextInt(max - min) + min
        }

        @Suppress("SpellCheckingInspection")
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
        private fun randomAlphanumericString(random: Random, maxLength: Int) =
            String(CharArray(random.nextInt(1, maxLength)) { CHARACTERS[random.nextInt(CHARACTERS.length)] })

        @JvmStatic
        fun randomStringGraph(random: Random, size: Int, maxLength: Int): UnweightedGraph<String> {
            require(size > 0) { "size must be positive: $size" }
            return randomGraph(List(size) { randomAlphanumericString(random, maxLength) }, random)
        }

        @JvmStatic
        fun randomStringGraph(size: Int, maxLength: Int): UnweightedGraph<String> {
            return randomStringGraph(Random(), size, maxLength)
        }

        @JvmStatic
        fun randomStringGraph(size: Int): UnweightedGraph<String> {
            return randomStringGraph(Random(), size, 32)
        }
    }
}
