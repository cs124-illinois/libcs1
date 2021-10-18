package com.example;

import cs125.graphs.Node;

import java.util.HashSet;
import java.util.Set;

public class Example {
  public static <T> int size(Node<T> graph) {
    Set<Node<T>> visited = new HashSet<>();
    traverse(graph, visited);
    return visited.size();
  }

  private static <T> void traverse(Node<T> node, Set<Node<T>> visited) {
    visited.add(node);
    for (Node<T> neighbor : node.getNeighbors()) {
      if (!(visited.contains(neighbor))) {
        traverse(neighbor, visited);
      }
    }
  }

  public static int sum(Node<Integer> graph) {
    Set<Node<Integer>> visited = new HashSet<>();
    traverse(graph, visited);
    int sum = 0;
    for (Node<Integer> node : visited) {
      sum += node.getValue();
    }
    return sum;
  }
}
