# libcs1

A Kotlin library providing educational data structures for CS1 courses, with a focus on graphs and trees. Designed for CS 124 and similar introductory computer science courses.

## Features

- **Graph Data Structures**: Comprehensive unweighted graph implementation with educational features
- **Binary Trees**: Support for balanced and unbalanced tree generation
- **Educational Design**: Built-in validation, lockable properties, and predictable testing
- **Java Interoperability**: Seamless usage from both Kotlin and Java
- **Extensive Factory Methods**: Pre-built graph types for various learning scenarios

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.github.cs124-illinois:libcs1:2025.6.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'com.github.cs124-illinois:libcs1:2025.6.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.github.cs124-illinois</groupId>
    <artifactId>libcs1</artifactId>
    <version>2025.6.0</version>
</dependency>
```

## Quick Start

### Creating Graphs

```kotlin
import cs1.graphs.UnweightedGraph

// Create a random undirected graph
val randomGraph = UnweightedGraph.random<Int>(size = 10)

// Create a complete graph
val completeGraph = UnweightedGraph.complete<Int>(size = 5)

// Create a linear graph (linked list structure)
val linearGraph = UnweightedGraph.linear(nodes = listOf(1, 2, 3, 4, 5))

// Create a circular graph
val circularGraph = UnweightedGraph.circular(nodes = setOf(1, 2, 3, 4))

// Check graph properties
println(randomGraph.isConnected())
println(randomGraph.hasCycle())
```

### Creating Binary Trees

```kotlin
import cs1.trees.BinaryTree

// Create a random balanced binary tree
val balancedTree = BinaryTree.randomBalanced<Int>(size = 15)

// Create a random unbalanced binary tree
val unbalancedTree = BinaryTree.randomUnbalanced<Int>(size = 10)

// Access tree properties
println(balancedTree.size)
```

### Java Interoperability

```java
import cs1.graphs.UnweightedGraph;
import cs1.graphs.GraphNode;

// All Kotlin methods are accessible from Java
UnweightedGraph<Integer> graph = UnweightedGraph.random(10);
boolean connected = graph.isConnected();

// Create custom graphs
GraphNode<String> nodeA = new GraphNode<>("A");
GraphNode<String> nodeB = new GraphNode<>("B");
UnweightedGraph<String> customGraph = new UnweightedGraph<>(
    Set.of(nodeA, nodeB),
    Map.of(nodeA, Set.of(nodeB))
);
```

## Educational Features

### Lockable Properties

Graphs support locking certain properties to create educational exercises:

```kotlin
val graph = UnweightedGraph.random<Int>(8)

// Lock edges - students can't access the edges directly
graph.edges = false

// Lock individual node access
graph.node = false

// Lock nodes collection access
graph.nodes = false
```

### Validation Methods

Built-in validation helps catch common student errors:

```kotlin
val graph = UnweightedGraph.random<Int>(10)

// Check if graph is properly connected
if (!graph.isConnected()) {
    println("Graph has disconnected components")
}

// Verify undirected graph property
if (!graph.isUndirected()) {
    println("Graph has asymmetric edges")
}

// Detect cycles
if (graph.hasCycle()) {
    println("Graph contains cycles")
}
```

### Predictable Testing

Nodes use nonce-based identity for predictable testing:

```kotlin
// Nodes with the same value are not equal unless they have the same nonce
val node1 = GraphNode(5)
val node2 = GraphNode(5)
assert(node1 != node2)  // Different nonces

// Copy constructor preserves nonce
val node3 = GraphNode(node1)
assert(node1 == node3)  // Same nonce
```

## Available Graph Types

The library provides various pre-built graph structures:

- `random(size)` - Random connected graph
- `complete(size)` - Fully connected graph
- `linear(nodes)` - Linked list structure
- `circular(nodes)` - Circular linked structure
- `cross(size)` - Cross-shaped graph
- `randomTwo(size)` - Random graph with exactly two edges per node
- `randomUndirectedConnected(size)` - Guaranteed connected undirected graph
- `randomDirectedConnected(size)` - Guaranteed connected directed graph
- `randomTree(size)` - Random tree structure

## Development

### Building from Source

```bash
# Clone the repository
git clone https://github.com/cs124-illinois/libcs1.git
cd libcs1

# Build the project
./gradlew build

# Run tests
./gradlew test

# Format code
./gradlew formatKotlin

# Run static analysis
./gradlew detekt
```

### Requirements

- Java 17 or higher (Java 21 recommended)
- Kotlin 2.1.0
- Gradle 8.14.1

### Testing

The library uses Kotest for testing:

```bash
# Run all tests
./gradlew test

# Run with increased memory for large graph tests
./gradlew test -Dkotest.framework.timeout=60000
```

## Contributing

Contributions are welcome! Please ensure:

1. All tests pass: `./gradlew test`
2. Code is formatted: `./gradlew formatKotlin`
3. Static analysis passes: `./gradlew detekt`

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the CS 124 course staff at the University of Illinois.

## Version History

This library uses date-based versioning (YYYY.M.patch):
- **2025.6.0** - Current version with enhanced graph validation and tree generation