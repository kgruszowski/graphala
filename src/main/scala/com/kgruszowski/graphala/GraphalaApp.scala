package com.kgruszowski.graphala

import com.kgruszowski.graphala.structure._

object GraphalaApp extends App {

  val graph = UndirectedGraph[String](
    List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m"),
    List(
      ("a", "b"),
      ("b", "e"),
      ("a", "c"),
      ("c", "d"),
      ("d", "e"),
      ("d", "f"),
      ("f", "g"),
      ("a", "g"),
      ("g", "h"),
      ("h", "i"),
      ("i", "j"),
      ("i", "k"),
      ("d", "j"),
      ("j", "l"),
      ("l", "m"),
      ("k", "m")
    )
  )

  println(graph.graph)
  println(UndirectedGraph.bfs(graph, "c"))
  println(UndirectedGraph.shortestPath(graph, "b", "l"))
}
