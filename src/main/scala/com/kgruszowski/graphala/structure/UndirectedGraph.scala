package com.kgruszowski.graphala.structure

import scala.collection.immutable.HashMap

case class UndirectedGraph[V](nodes: Seq[V], edges: Seq[(V, V)]) extends Graph[V] {

  case class Node(n: V)

  case class Edge(e1: V, e2: V)

  val graph: HashMap[Node, Seq[Edge]] = {
    val edgesPairs = for {
      e <- edges
      edgesPairs <- Seq(Edge(e._1, e._2), Edge(e._2, e._1))
    } yield edgesPairs

    def createHashMap(seq: Seq[Edge], adjList: HashMap[Node, Seq[Edge]]): HashMap[Node, Seq[Edge]] = seq match {
      case h :: t if adjList.contains(Node(h.e1)) => createHashMap(t, adjList + ((Node(h.e1), adjList(Node(h.e1)) :+ h)))
      case h :: t => createHashMap(t, adjList + (Node(h.e1) -> Seq(h)))
      case Nil => adjList
    }

    createHashMap(edgesPairs, HashMap[Node, Seq[Edge]]())
  }

  override def addNode(a: V): Graph[V] = UndirectedGraph(nodes.appended(a), edges)

  override def addEdge(a: V, b: V): Graph[V] = {
    (nodes.find(_ == a), nodes.find(_ == b)) match {
      case (Some(a), Some(b)) if !edges.contains((a, b)) && !edges.contains((b, a)) =>
        UndirectedGraph(nodes, edges.appended((a, b)))
      case _ => UndirectedGraph(nodes, edges)
    }
  }

  def getNeighbours(root: V): Seq[V] = {
    val iterableResult = for {
      n <- graph if n._1 == Node(root)
      e <- n._2
    } yield e.e2

    iterableResult.toSeq
  }

  override def addEdgeWithWeight(a: V, b: V, w: Int): Graph[V] = addEdge(a, b)
}

object UndirectedGraph {
  def bfs[V](graph: UndirectedGraph[V], src: V): Seq[V] = {
    def traverse(
                  graph: UndirectedGraph[V],
                  queue: List[V],
                  visited: Map[V, Boolean],
                  result: Seq[V]
                ): Seq[V] = queue match {
      case x :: xs if !visited(x) => traverse(
        graph,
        xs ++ graph.getNeighbours(x),
        visited + (x -> true),
        result :+ x
      )
      case _ :: xs => traverse(graph, xs, visited, result)
      case _ => result
    }

    val visitedNodes = graph.nodes.map(v => v -> false).toMap

    traverse(graph, List(src), visitedNodes, Seq[V]())
  }

  def shortestPath[V](graph: UndirectedGraph[V], src: V, dest: V): Seq[V] = {
    def traverse(
                  graph: UndirectedGraph[V],
                  activePredecessor: Seq[V],
                  predecessors: Map[V, V],
                  dest: V,
                  queue: Seq[V],
                  visited: Map[V, Boolean]
                ): Map[V, V] = {
      queue match {
        case x :: xs if !visited(x) => traverse(
          graph,
          activePredecessor.tail ++ Seq.fill(graph.getNeighbours(x).size)(x),
          predecessors + ((x, activePredecessor.head)),
          dest,
          xs ++ graph.getNeighbours(x),
          visited + (x -> true)
        )
        case _ :: xs => traverse(graph, activePredecessor.tail, predecessors, dest, xs, visited)
        case _ => predecessors
      }
    }

    val visitedNodes = graph.nodes.map(v => v -> false).toMap
    val paths = graph.nodes.map(v => v -> v).toMap

    val predecessors = traverse(graph, Seq[V](src), paths, dest, List(src), visitedNodes)

    def buildPath(predecessors: Map[V, V], path: Seq[V], src: V, dest: V): Seq[V] = {
      if (src == dest) {
        path.reverse
      } else {
        val newPath = path :+ predecessors(dest)

        buildPath(predecessors, newPath, src, predecessors(dest))
      }
    }

    buildPath(predecessors, Seq[V](dest), src, dest)
  }
}
