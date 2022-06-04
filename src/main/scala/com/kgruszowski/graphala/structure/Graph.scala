package com.kgruszowski.graphala
package structure

trait Graph[V] {
  def addNode(a: V): Graph[V]
  def addEdge(a: V, b: V): Graph[V]
}