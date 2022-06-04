package com.kgruszowski.graphala.structure

object GraphalaApp extends App {
  val graph = UndirectedGraph[Int](List(1, 2, 3, 4, 5), List((1, 3), (1, 4), (2, 3), (2, 5)))

  println(graph.addEdge(1, 4))

  val graphS = UndirectedGraph[String](List("kamil", "alan", "robert"), List(("kamil", "alan"), ("robert", "alan")))

  println(graphS.addNode("ed"))

  case class Vertex(value: Int)

  val v1 = Vertex(100)
  val v2 = Vertex(1000)
  val v3 = Vertex(1)
  val v4 = Vertex(0)

  val graphV = UndirectedGraph[Vertex](Vector(v1, v2, v3, v4), Seq((v1, v2), (v1, v3), (v3, v4)))

  println(graphV)
}
