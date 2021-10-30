import com.example.Example
import cs1.graphs.UnweightedGraph
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TestUnweightedGraph : StringSpec({
    "it should create a single-node graph" {
        UnweightedGraph.singleNodeGraph(8).also { graph ->
            Example.size(graph) shouldBe 1
            Example.sum(graph) shouldBe 8
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 1
                Example.sum(node) shouldBe 8
            }
        }
    }
    "it should create a two-node undirected graph" {
        UnweightedGraph.twoNodeUndirectedGraph(8, 16).also { graph ->
            Example.size(graph) shouldBe 2
            Example.sum(graph) shouldBe 24
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 2
                Example.sum(node) shouldBe 24
            }
        }
    }
    "it should create a two-node directed graph" {
        UnweightedGraph.twoNodeDirectedGraph(8, 16).also { graph ->
            Example.size(graph) shouldBe 2
            Example.sum(graph) shouldBe 24
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe if (node.value == 8) { 2 } else { 1 }
                Example.sum(node) shouldBe if (node.value == 8) { 24 } else { 16 }
            }
        }
    }
    "it should create a circular undirected graph" {
        UnweightedGraph.circleUndirectedGraph((0..31).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (0..31).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (0..31).sum()
            }
        }
    }
    "it should create a circular directed graph" {
        UnweightedGraph.circleDirectedGraph((0..31).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (0..31).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (0..31).sum()
            }
        }
    }
    "it should create a fully-connected undirected graph" {
        UnweightedGraph.fullyConnectedGraph((32..63).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (32..63).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (32..63).sum()
            }
        }
    }
    "it should create a random undirected graph" {
        UnweightedGraph.randomUndirectedGraph((32..63).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (32..63).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (32..63).sum()
            }
        }
        val graphs = mutableSetOf<UnweightedGraph<*>>()
        repeat(1024) {
            graphs += UnweightedGraph.randomUndirectedGraph((32..63).toList())
        }
        graphs.size shouldBe 1024
    }
    "it should create a random directed graph" {
        UnweightedGraph.randomDirectedGraph((32..63).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (32..63).sum()
            graph.edges.keys.find { node ->
                Example.size(node) == 32 && Example.sum(node) == (32..63).sum()
            } shouldNotBe null
        }
        val graphs = mutableSetOf<UnweightedGraph<*>>()
        repeat(1024) {
            graphs += UnweightedGraph.randomDirectedGraph((32..63).toList())
        }
        graphs.size shouldBe 1024
    }
    "it should create a random string graph" {
        UnweightedGraph.randomUndirectedStringGraph(1024)
    }
    "it should create equal graphs" {
        UnweightedGraph.fullyConnectedGraph((32..63).toList()).also { graph ->
            graph shouldBe UnweightedGraph.fullyConnectedGraph((32..63).toList())
            graph shouldNotBe UnweightedGraph.fullyConnectedGraph((31..62).toList())
            graph shouldNotBe UnweightedGraph.circleUndirectedGraph((32..63).toList())
        }
    }
    "it should copy graphs" {
        UnweightedGraph.fullyConnectedGraph((32..63).toList()).also { graph ->
            val copy = UnweightedGraph(graph)
            graph shouldBe UnweightedGraph(copy)
            (graph === copy) shouldBe false
            graph.edges.keys.forEach { node ->
                copy.edges[node] shouldNotBe null
                copy.edges.keys.find { it === node } shouldBe null
            }
        }
    }
    "it should lock nodes" {
        val graph = UnweightedGraph.randomUndirectedIntegerGraph(32)
        Example.size(graph.node) shouldBe Example.sizeWithNodes(graph)
        graph.lockNodes()
        shouldThrow<IllegalAccessException> {
            Example.sizeWithNodes(graph)
        }
    }
    "it should lock edges" {
        val graph = UnweightedGraph.randomUndirectedIntegerGraph(32)
        Example.size(graph.node) shouldBe Example.size(graph)
        graph.lockEdges()
        shouldThrow<IllegalAccessException> {
            Example.size(graph)
        }
    }
})
