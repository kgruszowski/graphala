package com.kgruszowski.graphala
package structure

import scala.collection.immutable.HashMap

case class UndirectedGraph[V](nodes: Seq[V], edges: Seq[(V, V)]) extends Graph[V] {

  val graph: HashMap[V, Seq[V]] = {
    val edgesPairs = for {
      e <- edges
      edgesPairs <- Seq(e, e.swap)
    } yield edgesPairs

    def createHashMap(seq: Seq[(V, V)], adjList: HashMap[V, Seq[V]]): HashMap[V, Seq[V]] = seq match {
      case h :: t if adjList.contains(h._1) => createHashMap(t, adjList + ((h._1, adjList(h._1) :+ h._2)))
      case h :: t => createHashMap(t, adjList + (h._1 -> Seq(h._2)))
      case Nil => adjList
    }

    createHashMap(edgesPairs, HashMap[V, Seq[V]]())
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
