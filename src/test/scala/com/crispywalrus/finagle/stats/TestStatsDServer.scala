package com.crispywalrus
package finagle
package stats

import java.net._
import scala.concurrent._, ExecutionContext.Implicits.global

/**
 * super simple server that listens for UDP packets on a port. It's
 * here so we can push metrics through censorinus and check the
 * output on the far side.
 */
trait TestStatsDServer {

  private[this] var running: Boolean = false
  private[this] var messages: List[String] = List()
  private[this] var socket: DatagramSocket = null

  def start(port: Int): Unit = {
    if (!running) {
      socket = new DatagramSocket(port)
      socket.setSoTimeout(10)
      running = true
      Future {
        listen(port)
      }
    }
  }

  def stop: Unit = {
    running = false
    socket.close()
    socket = null
  }

  def extract: List[String] = {
    val l: List[String] = messages
    messages = List()
    l
  }

  def listen(port: Int): Boolean = {
    while (running) {
      val buffer = new Array[Byte](1024)
      val packet = new DatagramPacket(buffer, 1024)
      try {
        blocking {
          socket.receive(packet)
          val msg = new String(packet.getData, 0, packet.getLength)
          messages = messages :+ msg
        }
      } catch {
        case ste: SocketTimeoutException => ()
        case ex: Throwable => throw ex
      }
    }
    running
  }
}

object TestStatsDServer extends TestStatsDServer
