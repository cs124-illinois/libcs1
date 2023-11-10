package cs1.graphs

import java.util.Objects
import java.util.Random

class Node<T>(var value: T, val nonce: Int) {
    override fun equals(other: Any?) =
        when (other) {
            !is Node<*> -> false
            else -> value == other.value && nonce == other.nonce
        }

    override fun hashCode() = Objects.hash(value, nonce)

    override fun toString(): String {
        return "Node(value=$value)"
    }
}

class GraphNode<T>
    @JvmOverloads
    constructor(val value: T, val nonce: Int, var neighbors: Set<GraphNode<T>> = setOf()) {
        constructor(node: GraphNode<T>) : this(node.value, node.nonce)

        override fun equals(other: Any?) =
            when (other) {
                !is GraphNode<*> -> false
                else -> value == other.value && nonce == other.nonce
            }

        override fun hashCode() = Objects.hash(value, nonce)

        override fun toString(): String {
            return "GraphNode(value=$value)"
        }
    }

private fun <T> GraphNode<T>.find(): Set<GraphNode<T>> =
    mutableSetOf<GraphNode<T>>().also { nodes ->
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
fun <T> Map<Node<T>, Set<Node<T>>>.toGraphNodes(
    random: Random,
    checkUndirected: Boolean,
) = keys.associateWith { GraphNode(it.value, random.nextInt()) }.let { mapping ->
    map { (key, values) ->
        check(mapping[key] != null) { "Missing mapping for node in graph creation" }
        mapping[key]!! to
            values.map { value ->
                check(mapping[value] != null) { "Missing mapping for node in graph creation" }
                mapping[value]!!
            }.toSet()
    }.toMap().apply {
        forEach { (node, neighbors) ->
            node.neighbors = neighbors
        }
        if (checkUndirected) {
            check(keys.first().find() == mapping.values.toSet()) { "Graph is not connected" }
        } else {
            check(keys.find { it.find() == mapping.values.toSet() } != null) { "Graph is not connected" }
        }
        keys.forEach { node ->
            check(node !in node.neighbors) { "Graph contains a self-edge" }
            if (checkUndirected) {
                for (neighbor in node.neighbors) {
                    check(node in neighbor.neighbors) { "Graph is not undirected" }
                }
            }
        }
    }
}

private fun <T> Map<GraphNode<T>, Set<GraphNode<T>>>.copyGraphNodes() =
    keys.associateWith { GraphNode(it) }.let { mapping ->
        map { (key, values) ->
            check(mapping[key] != null) { "Missing mapping for node in graph copy" }
            mapping[key]!! to
                values.map { value ->
                    check(mapping[value] != null) { "Missing mapping for node in graph creation" }
                    mapping[value]!!
                }.toSet()
        }.toMap().onEach { (node, neighbors) ->
            node.neighbors = neighbors
        }
    }

fun Map<GraphNode<*>, Set<GraphNode<*>>>.toNodes() =
    keys.associateWith { Node(it.value, 0) }.let { mapping ->
        map { (key, values) ->
            check(mapping[key] != null) { "Missing mapping for node in graph creation" }
            mapping[key]!! to
                values.map { value ->
                    check(mapping[value] != null) { "Missing mapping for node in graph creation" }
                    mapping[value]!!
                }.toSet()
        }.toMap()
    }

@Suppress("unused")
class UnweightedGraph<T> private constructor(edges: Map<GraphNode<T>, Set<GraphNode<T>>>) {
    constructor(edges: Map<Node<T>, Set<Node<T>>>, random: Random = Random(), isUndirected: Boolean) :
        this(edges.toGraphNodes(random, isUndirected))

    constructor(graph: UnweightedGraph<T>) :
        this(graph.edges.copyGraphNodes())

    private val _edges = edges
    val edges = edges
        get() {
            if (edgesLocked) {
                throw IllegalAccessException("edges may not be accessed in this graph")
            }
            return field
        }
    private var edgesLocked = false

    fun lockEdges(): UnweightedGraph<T> {
        edgesLocked = true
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) =
        when (other) {
            !is UnweightedGraph<*> -> false
            else ->
                (_edges as Map<GraphNode<*>, Set<GraphNode<*>>>).toNodes() ==
                    (other._edges as Map<GraphNode<*>, Set<GraphNode<*>>>).toNodes()
        }

    override fun hashCode() = Objects.hash(_edges)

    val node: GraphNode<T>
        get() {
            if (nodeLocked) {
                throw IllegalAccessException("node may not be accessed in this graph")
            }
            return _edges.keys.minByOrNull { it.nonce }!!
        }
    private var nodeLocked = false

    fun lockNode(): UnweightedGraph<T> {
        nodeLocked = true
        return this
    }

    val nodes: Set<GraphNode<T>>
        get() {
            if (nodesLocked) {
                throw IllegalAccessException("nodes may not be accessed in this graph")
            }
            return _edges.keys
        }
    private var nodesLocked = false

    fun lockNodes(): UnweightedGraph<T> {
        nodesLocked = true
        return this
    }

    @Suppress("TooManyFunctions")
    companion object {
        @JvmOverloads
        @JvmStatic
        fun <T> singleNodeGraph(
            value: T,
            random: Random = Random(124),
        ) = UnweightedGraph(mapOf(Node(value, 0) to setOf()), random, true)

        @JvmOverloads
        @JvmStatic
        fun <T> twoNodeUndirectedGraph(
            first: T,
            second: T,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            val mapping = mapOf(0 to Node(first, 0), 1 to Node(second, 1))
            return UnweightedGraph(
                mapOf(
                    mapping[0]!! to setOf(mapping[1]!!),
                    mapping[1]!! to setOf(mapping[0]!!),
                ),
                random,
                true,
            )
        }

        @JvmOverloads
        @JvmStatic
        fun <T> twoNodeDirectedGraph(
            first: T,
            second: T,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            val mapping = mapOf(0 to Node(first, 0), 1 to Node(second, 1))
            return UnweightedGraph(
                mapOf(
                    mapping[0]!! to setOf(mapping[1]!!),
                    mapping[1]!! to setOf(),
                ),
                random,
                false,
            )
        }

        @JvmOverloads
        @JvmStatic
        fun <T> linearUndirectedGraph(
            list: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> linearDirectedGraph(
            list: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
            }
            return UnweightedGraph(edges, random, false)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> crossUndirectedGraph(
            first: List<T>,
            second: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(first.size >= 2 && second.size > 2) { "List has fewer than two elements" }
            val mapping = (first + second).mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (first.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }
            val secondIndices = (first.size until (first.size + second.size)).toList()
            assert(secondIndices.size == second.size)
            val secondFirstHalf = secondIndices.subList(0, secondIndices.size / 2)
            val secondSecondHalf = secondIndices.subList(secondIndices.size / 2, secondIndices.size)

            for (i in secondFirstHalf.first() until secondFirstHalf.last()) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }
            for (i in secondSecondHalf.first() until secondSecondHalf.last()) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }

            val midPoint = first.size / 2
            edges[mapping[midPoint]]!! += mapping[secondFirstHalf.last()]!!
            edges[mapping[midPoint]]!! += mapping[secondSecondHalf.first()]!!
            edges[mapping[secondFirstHalf.last()]]!! += mapping[midPoint]!!
            edges[mapping[secondSecondHalf.first()]]!! += mapping[midPoint]!!
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> randomTreeUndirectedGraph(
            list: List<T>,
            random: Random = Random(),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                val previous = ((i + 1) until list.size).toList()
                if (previous.isEmpty()) {
                    continue
                }
                for (j in previous.shuffled(random).take(1)) {
                    edges[mapping[i]]!! += mapping[j]!!
                    edges[mapping[j]]!! += mapping[i]!!
                }
            }
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> circleUndirectedGraph(
            list: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
                edges[mapping[i + 1]]!! += mapping[i]!!
            }
            edges[mapping[0]]!! += mapping[list.size - 1]!!
            edges[mapping[list.size - 1]]!! += mapping[0]!!
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> circleDirectedGraph(
            list: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[i]]!! += mapping[i + 1]!!
            }
            edges[mapping[list.size - 1]]!! += mapping[0]!!
            return UnweightedGraph(edges, random, false)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> fullyConnectedGraph(
            list: List<T>,
            random: Random = Random(124),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                for (j in (i + 1) until list.size) {
                    edges[mapping[i]]!! += mapping[j]!!
                    edges[mapping[j]]!! += mapping[i]!!
                }
            }
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> randomUndirectedGraph(
            list: List<T>,
            random: Random = Random(),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                for (j in (list.indices - i).shuffled(random).take(random.nextInt(list.size) + 1)) {
                    edges[mapping[i]]!! += mapping[j]!!
                    edges[mapping[j]]!! += mapping[i]!!
                }
            }
            return UnweightedGraph(edges, random, true)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> randomDirectedGraph(
            list: List<T>,
            random: Random = Random(),
        ): UnweightedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.mapIndexed { i, item -> i to Node(item, i) }.toMap()
            val edges = mapping.values.associateWith { mutableSetOf<Node<T>>() }
            for (i in list.indices) {
                for (j in (list.indices - i).shuffled(random).take(random.nextInt(list.size) + 1)) {
                    edges[mapping[i]]!! += mapping[j]!!
                }
            }
            return UnweightedGraph(edges, random, false)
        }

        @JvmStatic
        fun randomUndirectedIntegerGraph(
            random: Random,
            size: Int,
            maxInteger: Int,
        ): UnweightedGraph<Int> {
            require(size > 0) { "size must be positive: $size" }
            return randomUndirectedGraph(List(size) { random.nextInt(maxInteger) - (maxInteger / 2) }, random)
        }

        @JvmStatic
        fun randomUndirectedIntegerGraph(
            size: Int,
            maxInteger: Int,
        ): UnweightedGraph<Int> {
            return randomUndirectedIntegerGraph(Random(), size, maxInteger)
        }

        @JvmStatic
        fun randomUndirectedIntegerGraph(size: Int): UnweightedGraph<Int> {
            return randomUndirectedIntegerGraph(Random(), size, 128)
        }

        private fun Random.nextIntRange(
            min: Int,
            max: Int,
        ) = let {
            require(min < max)
            nextInt(max - min) + min
        }

        @Suppress("SpellCheckingInspection")
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "

        private fun randomAlphanumericString(
            random: Random,
            maxLength: Int,
        ) = String(CharArray(random.nextInt(1, maxLength)) { CHARACTERS[random.nextInt(CHARACTERS.length)] })

        @JvmStatic
        fun randomUndirectedStringGraph(
            random: Random,
            size: Int,
            maxLength: Int,
        ): UnweightedGraph<String> {
            require(size > 0) { "size must be positive: $size" }
            return randomUndirectedGraph(List(size) { randomAlphanumericString(random, maxLength) }, random)
        }

        @JvmStatic
        fun randomUndirectedStringGraph(
            size: Int,
            maxLength: Int,
        ): UnweightedGraph<String> {
            return randomUndirectedStringGraph(Random(), size, maxLength)
        }

        @JvmStatic
        fun randomUndirectedStringGraph(size: Int): UnweightedGraph<String> {
            return randomUndirectedStringGraph(Random(), size, 32)
        }
    }
}
