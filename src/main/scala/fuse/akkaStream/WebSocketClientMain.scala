package fuse.akkaStream

import akka.actor.{Props, ActorSystem}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source
import fuse.akkaStream.http.{WebSocket, TestingWebSocketClient}

import scala.util.{Failure, Success}

object WebSocketClientMain {

  def main(args: Array[String]): Unit = {
    run
  }

  def run: scala.Unit = {
    implicit val system = ActorSystem("Sys")

    val wsh = system.actorOf(Props(new TestingWebSocketClient {
      override def businessLogic = {
        case WebSocket.Send(message) =>
          log.info("Client sending message {}", message)
          send(message)
        case WebSocket.Release => close
        case whatever => // ignore
      }
    }))
    import system.dispatcher

    println("before connection")
    wsh ! WebSocket.Connect("localhost", 9002, "/ok")
    Thread.sleep(2000L) // wait for all servers to be cleanly started
    wsh ! WebSocket.Send("22.1523721 41.4140567")
    Thread.sleep(1000L)
    println("after send")
    wsh ! WebSocket.Release

//    implicit val materializer = ActorFlowMaterializer()
//
//    val localActor = system.actorOf(Props[LocalActor], name = "local")
//
//    val text =
//      """|Lorem Ipsum is simply dummy text of the printing and typesetting industry.
//        |Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
//        |when an unknown printer took a galley of type and scrambled it to make a type
//        |specimen book.""".stripMargin
//
//    Source(() => text.split("\\s").iterator).
//      map(_.toUpperCase).
//      // filter(line => line.length > 3).
//      runForeach(localActor ! _).
//      onComplete {
//        case Success(result) =>
//          println("Shutting down client")
//          system.shutdown()
//          run
//        case Failure(e) =>
//          println("Failure: " + e.getMessage)
//          system.shutdown()
//      }
  }
}
