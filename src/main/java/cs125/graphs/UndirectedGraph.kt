package cs125.graphs

class Node<T>(val value: T, private val index: Int, val neighbors: MutableSet<Node<T>> = mutableSetOf()) {
    override fun toString() = "Node $index ($value)"
}

class InputNode<T>(var value: T)

private fun <T> find(node: Node<T>, visited: MutableSet<Node<T>>) {
    visited += node
    for (neighbor in node.neighbors) {
        if (neighbor !in visited) {
            find(neighbor, visited)
        }
    }
}

private fun <T> find(node: Node<T>): Set<Node<T>> {
    val nodes = mutableSetOf<Node<T>>()
    find(node, nodes)
    return nodes
}

fun <T> Map<InputNode<T>, Set<InputNode<T>>>.toGraph(): Node<T> {
    check(isNotEmpty()) { "Graph is empty" }
    val mapping = keys.mapIndexed { index, key -> key to Node(key.value, index) }.toMap()
    forEach { (key, values) ->
        check(mapping[key] != null) { "Missing mapping for key in graph creation" }
        mapping[key]!!.neighbors.addAll(
            values.map {
                mapping[it] ?: error("Missing mapping for value in graph creation")
            }
        )
    }

    mapping.values.forEach {
        check(it !in it.neighbors) { "Graph contains a self-edge" }
        check(find(it) == mapping.values.toSet()) { "Graph is not connected" }
        for (node in it.neighbors) {
            check(it in node.neighbors) { "Graph is not undirected" }
        }
    }
    return mapping.values.first()
}

fun <T> singleNodeGraph(value: T) = mapOf(InputNode(value) to setOf<InputNode<T>>()).toGraph()

fun <T> twoNodeGraph(first: T, second: T): Node<T> {
    val mapping = mapOf(first to InputNode(first), second to InputNode(second))
    return mapOf(
        mapping[first]!! to setOf(mapping[second]!!),
        mapping[second]!! to setOf(mapping[first]!!)
    ).toGraph()
}

fun <T> circleGraph(list: List<T>): Node<T> {
    require(list.size >= 2) { "List has fewer than two elements" }
    val mapping = list.associateWith {
        InputNode(it)
    }
    val edges = mapping.values.associateWith { mutableSetOf<InputNode<T>>() }
    for (i in 0 until (list.size - 1)) {
        edges[mapping[list[i]]]!! += mapping[list[i + 1]]!!
        edges[mapping[list[i + 1]]]!! += mapping[list[i]]!!
    }
    edges[mapping[list[0]]]!! += mapping[list[list.size - 1]]!!
    edges[mapping[list[list.size - 1]]]!! += mapping[list[0]]!!
    return edges.toGraph()
}

fun <T> fullyConnectedGraph(list: List<T>): Node<T> {
    require(list.size >= 2) { "List has fewer than two elements" }
    val mapping = list.associateWith {
        InputNode(it)
    }
    val edges = mapping.values.associateWith { mutableSetOf<InputNode<T>>() }
    for (i in list.indices) {
        for (j in list.indices - i) {
            edges[mapping[list[i]]]!! += mapping[list[j]]!!
            edges[mapping[list[j]]]!! += mapping[list[i]]!!
        }
    }
    return edges.toGraph()
}

/*
class UndirectedGraph<T>(inputEdges: Map<InputNode<T>, Set<InputNode<T>>>) {
    private val edges: Map<Node, Set<Node>>
    val node: Node

    init {
        check(inputEdges.isNotEmpty()) { "Graph is empty" }
        val edgeMapping = inputEdges.keys.mapIndexed { index, key ->
            key to Node(key, index)
        }.toMap()
        edges = inputEdges.map { (key, values) ->
            check(edgeMapping[key] != null) { "Missing mapping for key in graph creation" }
            edgeMapping[key]!! to values.map { value ->
                edgeMapping[value] ?: error("Missing mapping for value during graph creation")
            }.toSet()
        }.toMap()

        for ((first, neighbors) in edges) {
            check(first !in neighbors) { "Graph contains a self-edge" }
            for (second in neighbors) {
                check(second.connectedTo(first)) { "Graph is not undirected" }
            }
            check(first.find() == edges.keys) { "Graph is not connected" }
        }

        node = edges.keys.first()
    }

    inner class Node(var value: T, private val index: Int) {
        constructor(graphNode: InputNode<T>, index: Int) : this(graphNode.value, index)

        val neighbors: Set<Node>
            get() = edges[this]!!

        internal fun connectedTo(second: Node) = edges[this]?.contains(second) ?: false
        internal fun find(): Set<Node> {
            val nodes = mutableSetOf<Node>()
            find(nodes)
            return nodes
        }

        private fun find(visited: MutableSet<Node>) {
            visited += this
            for (neighbor in neighbors) {
                if (neighbor !in visited) {
                    neighbor.find(visited)
                }
            }
        }

        override fun toString() = "Node $index ($value)"
    }

    companion object {
        @JvmStatic
        fun <T> singleNodeGraph(value: T) = UndirectedGraph(mapOf(InputNode(value) to setOf()))

        @JvmStatic
        fun <T> twoNodeGraph(first: T, second: T): UndirectedGraph<T> {
            val mapping = mapOf(first to InputNode(first), second to InputNode(second))
            return UndirectedGraph(
                mapOf(
                    mapping[first]!! to setOf(mapping[second]!!),
                    mapping[second]!! to setOf(mapping[first]!!)
                )
            )
        }

        @JvmStatic
        fun <T> circleGraph(list: List<T>): UndirectedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.associateWith {
                InputNode(it)
            }
            val edges = mapping.values.associateWith { mutableSetOf<InputNode<T>>() }
            for (i in 0 until (list.size - 1)) {
                edges[mapping[list[i]]]!! += mapping[list[i + 1]]!!
                edges[mapping[list[i + 1]]]!! += mapping[list[i]]!!
            }
            edges[mapping[list[0]]]!! += mapping[list[list.size - 1]]!!
            edges[mapping[list[list.size - 1]]]!! += mapping[list[0]]!!
            return UndirectedGraph(edges)
        }

        @JvmStatic
        fun <T> fullyConnectedGraph(list: List<T>): UndirectedGraph<T> {
            require(list.size >= 2) { "List has fewer than two elements" }
            val mapping = list.associateWith {
                InputNode(it)
            }
            val edges = mapping.values.associateWith { mutableSetOf<InputNode<T>>() }
            for (i in list.indices) {
                for (j in list.indices - i) {
                    edges[mapping[list[i]]]!! += mapping[list[j]]!!
                    edges[mapping[list[j]]]!! += mapping[list[i]]!!
                }
            }
            return UndirectedGraph(edges)
        }
    }
}
*/
