import com.example.Example
import cs1.graphs.UnweightedGraph
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TestUnweightedGraph :
    StringSpec({
        "it should create a single-node graph" {
            UnweightedGraph.singleNodeGraph(8).also { graph ->
                Example.size(graph) shouldBe 1
                Example.sum(graph) shouldBe 8
                Example.hasCycle(graph) shouldBe false
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
                Example.hasCycle(graph) shouldBe false
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
                Example.hasCycle(graph) shouldBe false
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe
                        if (node.value == 8) {
                            2
                        } else {
                            1
                        }
                    Example.sum(node) shouldBe
                        if (node.value == 8) {
                            24
                        } else {
                            16
                        }
                }
            }
        }
        "it should create a circular undirected graph" {
            UnweightedGraph.circleUndirectedGraph((0..31).toList()).also { graph ->
                Example.size(graph) shouldBe 32
                Example.sum(graph) shouldBe (0..31).sum()
                Example.hasCycle(graph) shouldBe true
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe 32
                    Example.sum(node) shouldBe (0..31).sum()
                }
            }
        }
        "it should create a small circular undirected graph" {
            UnweightedGraph.circleUndirectedGraph((0..2).toList()).also { graph ->
                Example.hasCycle(graph) shouldBe true
            }
        }
        "it should create a circular directed graph" {
            UnweightedGraph.circleDirectedGraph((0..31).toList()).also { graph ->
                Example.size(graph) shouldBe 32
                Example.sum(graph) shouldBe (0..31).sum()
                Example.hasCycle(graph) shouldBe true
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe 32
                    Example.sum(node) shouldBe (0..31).sum()
                }
            }
        }
        "it should create a linear undirected graph" {
            UnweightedGraph.linearUndirectedGraph((0..31).toList()).also { graph ->
                Example.size(graph) shouldBe 32
                Example.sum(graph) shouldBe (0..31).sum()
                Example.hasCycle(graph) shouldBe false
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe 32
                    Example.sum(node) shouldBe (0..31).sum()
                }
            }
        }
        "it should create a small linear undirected graph" {
            UnweightedGraph.linearUndirectedGraph((0..2).toList()).also { graph ->
                Example.hasCycle(graph) shouldBe false
            }
        }
        "it should create a linear directed graph" {
            UnweightedGraph.linearDirectedGraph((0..31).toList()).also { graph ->
                Example.size(graph) shouldBe 32
                Example.sum(graph) shouldBe (0..31).sum()
                Example.hasCycle(graph) shouldBe false
                graph.edges.keys.filter { node ->
                    Example.size(node) == 32 && Example.sum(node) == (0..31).sum()
                } shouldHaveSize 1
            }
        }
        "it should create a small cross undirected graph" {
            UnweightedGraph.crossUndirectedGraph((0..2).toList(), (3..5).toList()).also { graph ->
                Example.size(graph) shouldBe 6
                Example.sum(graph) shouldBe (0..5).sum()
                Example.hasCycle(graph) shouldBe false
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe 6
                    Example.sum(node) shouldBe (0..5).sum()
                }
            }
        }
        "it should create a random tree undirected graph" {
            UnweightedGraph.randomTreeUndirectedGraph((0..31).toList()).also { graph ->
                Example.size(graph) shouldBe 32
                Example.sum(graph) shouldBe (0..31).sum()
                Example.hasCycle(graph) shouldBe false
                graph.edges.keys.forEach { node ->
                    Example.size(node) shouldBe 32
                    Example.sum(node) shouldBe (0..31).sum()
                }
            }
            val graphs = mutableSetOf<UnweightedGraph<*>>()
            repeat(1024) {
                val newGraph = UnweightedGraph.randomTreeUndirectedGraph((32..63).toList())
                Example.hasCycle(newGraph) shouldBe false
                graphs += newGraph
            }
            graphs.size shouldBe 1024
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
                val newGraph = UnweightedGraph.randomUndirectedGraph((32..63).toList())
                // Not 100%, but almost always...
                Example.hasCycle(newGraph) shouldBe true
                graphs += newGraph
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
