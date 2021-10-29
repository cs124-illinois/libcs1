import com.example.Example
import cs1.graphs.UnweightedGraph
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
    "it should create a two-node graph" {
        UnweightedGraph.twoNodeGraph(8, 16).also { graph ->
            Example.size(graph) shouldBe 2
            Example.sum(graph) shouldBe 24
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 2
                Example.sum(node) shouldBe 24
            }
        }
    }
    "it should create a circular graph" {
        UnweightedGraph.circleGraph((0..31).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (0..31).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (0..31).sum()
            }
        }
    }
    "it should create a fully-connected graph" {
        UnweightedGraph.fullyConnectedGraph((32..63).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (32..63).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (32..63).sum()
            }
        }
    }
    "it should create a random graph" {
        UnweightedGraph.randomGraph((32..63).toList()).also { graph ->
            Example.size(graph) shouldBe 32
            Example.sum(graph) shouldBe (32..63).sum()
            graph.edges.keys.forEach { node ->
                Example.size(node) shouldBe 32
                Example.sum(node) shouldBe (32..63).sum()
            }
        }
        val graphs = mutableSetOf<UnweightedGraph<*>>()
        repeat(1024) {
            graphs += UnweightedGraph.randomGraph((32..63).toList())
        }
        graphs.size shouldBe 1024
    }
    "it should create equal graphs" {
        UnweightedGraph.fullyConnectedGraph((32..63).toList()).also { graph ->
            graph shouldBe UnweightedGraph.fullyConnectedGraph((32..63).toList())
            graph shouldNotBe UnweightedGraph.fullyConnectedGraph((31..62).toList())
            graph shouldNotBe UnweightedGraph.circleGraph((32..63).toList())
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
})
