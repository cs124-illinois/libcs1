import com.example.Example
import cs1.graphs.circleGraph
import cs1.graphs.fullyConnectedGraph
import cs1.graphs.singleNodeGraph
import cs1.graphs.toMap
import cs1.graphs.twoNodeGraph
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestUndirectedGraph : StringSpec({
    "f: it should create a single-node graph" {
        singleNodeGraph(8).also {
            println(it.toMap())
            //Example.size(it) shouldBe 1
            //Example.sum(it) shouldBe 8
        }
    }
    "it should create a two-node graph" {
        twoNodeGraph(8, 16).also {
            Example.size(it) shouldBe 2
            Example.sum(it) shouldBe 24
        }
    }
    "it should create a circular graph" {
        circleGraph((0..31).toList()).also {
            Example.size(it) shouldBe 32
            Example.sum(it) shouldBe (0..31).sum()
        }
    }
    "it should create a fully-connected graph" {
        fullyConnectedGraph((32..63).toList()).also {
            Example.size(it) shouldBe 32
            Example.sum(it) shouldBe (32..63).sum()
        }
    }
})
