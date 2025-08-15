# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

libcs1 is an educational Kotlin library providing CS1 data structures for the CS 124 course. The library focuses on graph and tree implementations with educational features like lockable properties and extensive factory methods for generating test cases.

## Build and Development Commands

```bash
# Build and test
./gradlew build          # Full build with tests
./gradlew test           # Run all tests
./gradlew clean          # Clean build directory

# Code quality - run these before committing
./gradlew detekt         # Static analysis
./gradlew formatKotlin   # Auto-format code
./gradlew lintKotlin     # Lint checks

# Publishing
./gradlew publishToMavenLocal    # Local Maven publishing for testing
./gradlew publish                # Publish to Sonatype/Maven Central

# Dependency management
./gradlew dependencyUpdates      # Check for newer dependency versions
```

## Architecture and Key Design Patterns

### Core Data Structures

The library implements educational data structures with specific design choices:

1. **Graph Implementation** (`cs1.graphs.UnweightedGraph`):
   - Uses nonce-based node identity (random integers) instead of reference equality for predictable testing
   - Implements lockable properties (`edges`, `node`, `nodes`) that can be restricted for educational exercises
   - Provides extensive static factory methods for creating various graph types (linear, circular, cross, random)
   - Includes comprehensive validation methods (isConnected, isUndirected, hasCycle)

2. **Binary Tree Implementation** (`cs1.trees.BinaryTree`):
   - Supports both balanced and unbalanced random tree generation
   - Maintains internal size tracking for performance
   - Uses template method pattern for different generation strategies

### Educational Design Choices

- **Seeded Randomness**: Many factory methods use seed 124 for reproducible results in educational contexts
- **Defensive Copying**: Graph nodes and edges are defensively copied to prevent external modification
- **Java Interoperability**: All Kotlin classes use `@JvmStatic` and `@JvmOverloads` for seamless Java usage
- **Validation-First**: Built-in property checks help catch student implementation errors early

### Cross-Language Testing

Tests are written in both Kotlin (using Kotest) and Java to ensure compatibility. The `Example.java` class in tests demonstrates Java interoperability.

## Testing Approach

- **Framework**: Kotest 5.9.1 with StringSpec style
- **Test Memory**: Tests run with `-Xmx1G -Xss256k`
- **Property Testing**: Tests verify graph properties like connectivity, cycles, and sizes
- **Randomness Testing**: Ensures unique generation across multiple invocations

## Version Management

The project uses date-based versioning: `YYYY.M.patch` (e.g., 2025.6.0). Version is defined in `build.gradle.kts`.

## Key Files and Locations

- Main graph implementation: `src/main/java/cs1/graphs/UnweightedGraph.kt`
- Main tree implementation: `src/main/java/cs1/trees/BinaryTree.kt`
- Build configuration: `build.gradle.kts`
- Static analysis config: `config/detekt/detekt.yml`
- Java version: 21.0.2 (specified in `.tool-versions`)