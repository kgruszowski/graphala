package com.kgruszowski.graphala
package structure

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
    (nodes.find(_ == a), nodes.find(_ == b )) match {
      case (Some(a), Some(b)) if !edges.contains((a,b)) && !edges.contains((b,a)) =>
        UndirectedGraph(nodes, edges.appended((a, b)))
      case _ => UndirectedGraph(nodes, edges)
    }
  }
}

object UndirectedGraph {

}
