package com.kgruszowski.graphala.structure

import scala.collection.immutable.HashMap

case class UndirectedWeightedGraph[V](nodes: Seq[V], edges: Seq[(V, V, Int)]) extends Graph[V] {

  case class Node(n: V)
  case class Edge(e1: V, e2: V, w: Int)

  val graph: HashMap[Node, Seq[Edge]] = {
    val edgesPairs = for {
      e <- edges
      edgesPairs <- Seq(Edge(e._1, e._2, e._3), Edge(e._2, e._1, e._3))
    } yield edgesPairs

    def createHashMap(seq: Seq[Edge], adjList: HashMap[Node, Seq[Edge]]): HashMap[Node, Seq[Edge]] = seq match {
      case h :: t if adjList.contains(Node(h.e1)) => createHashMap(t, adjList + ((Node(h.e1), adjList(Node(h.e1)) :+ h)))
      case h :: t => createHashMap(t, adjList + (Node(h.e1) -> Seq(h)))
      case Nil => adjList
    }

    createHashMap(edgesPairs, HashMap[Node, Seq[Edge]]())
  }

  override def addNode(a: V): Graph[V] = UndirectedWeightedGraph(nodes.appended(a), edges)

  override def addEdge(a: V, b: V): Graph[V] = addEdgeWithWeight(a, b, 1)

  override def addEdgeWithWeight(a: V, b: V, w: Int): Graph[V] = {
    (nodes.find(_ == a), nodes.find(_ == b )) match {
      case (Some(a), Some(b)) if !edges.contains((a,b,w)) && !edges.contains((b,a,w)) =>
        UndirectedWeightedGraph(nodes, edges.appended((a, b, w)))
      case _ => UndirectedWeightedGraph(nodes, edges)
    }
  }
}

object UndirectedWeightedGraph {

}