package fuse.akkaStream

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import akka.actor.{Props, ActorSystem}
import akka.serialization.{SerializationExtension, Serializer}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source
import akka.util.{CompactByteString, ByteString}
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.ByteArraySerializer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fuse.akkaStream.http.{WebSocket, TestingWebSocketClient}
import org.bytedeco.javacv.{Frame, OpenCVFrameGrabber}
import spray.can.websocket.frame.BinaryFrame

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

    val grabber = new OpenCVFrameGrabber("data/data.mp4");
    grabber.start()

    val source: Source[Frame, Unit] =
      Source(() => Iterator.continually(grabber.grab())) ;

    println("before connection")
    wsh ! WebSocket.Connect("localhost", 9002, "/ok")
    Thread.sleep(2000L) // wait for websocket to connect

    val gson:Gson = new Gson

    try {
    Iterator.continually(grabber.grab()).foreach(frame=>{
      wsh ! WebSocket.Send(gson.toJson(frame))
    })
    } catch {
      case _ => println("End of stream")
    }

    Thread.sleep(1000L)
    println("after send")
    wsh ! WebSocket.Release
  }
}
