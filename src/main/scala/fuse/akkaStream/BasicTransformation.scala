package fuse.akkaStream

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source

import scala.util.{ Failure, Success }

object BasicTransformation {

  def main(args: Array[String]): Unit = {
    run
  }

  def run: scala.Unit = {
    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = ActorFlowMaterializer()

    val text =
      """|Lorem Ipsum is simply dummy text of the printing and typesetting industry.
        |Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
        |when an unknown printer took a galley of type and scrambled it to make a type
        |specimen book.""".stripMargin

    Source(() => text.split("\\s").iterator).
      map(_.toUpperCase).
      // filter(line => line.length > 3).
      runForeach(println).
      onComplete {
        case Success(result) =>
          println("Shutting down client")
          system.shutdown()
          run
        case Failure(e) =>
          println("Failure: " + e.getMessage)
          system.shutdown()
      }
  }
}
