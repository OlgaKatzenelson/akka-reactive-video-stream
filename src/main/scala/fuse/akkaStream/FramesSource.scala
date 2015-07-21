package fuse.akkaStream

import org.bytedeco.javacv.Frame
import akka.stream.scaladsl.Source
import org.bytedeco.javacv.OpenCVFrameGrabber
import akka.stream.scaladsl.Sink
import org.bytedeco.javacv.CanvasFrame
import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.RunnableFlow
import akka.stream.scaladsl.FlowGraph
import akka.stream.scaladsl.Broadcast
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.javacpp.opencv_core.IplImage

object FramesSource extends App {


  //  def main(args: Array[String]): Unit = {
  //    run
  //  }
  //
  //  def run: scala.Unit = {
  implicit val system = ActorSystem("Sys")
  import system.dispatcher

  implicit val materializer = ActorFlowMaterializer()

  val canvas = new CanvasFrame("My Video", 1);

  val grabber = new OpenCVFrameGrabber("data/data.mp4");
  grabber.start()

  val source: Source[Frame, Unit] =
    Source(() => Iterator.continually(grabber.grab()));

  

  //val sink = Sink.foreach { f: Frame => canvas.showImage(f) }
  val sink = Sink.foreach { f: Frame => canvas.showImage(convert(f)) }

  def convert(f : Frame) : Frame = {
    f
  }

  //    val g = FlowGraph.closed() { implicit b =>
  //      import FlowGraph.Implicits._
  //
  //      val bcast = b.add(Broadcast[Frame](1))
  //      source ~> bcast.in
  //      bcast.out(0) ~> Flow[Frame] ~> sink
  //    }
  //    g.run()

  source.runWith(sink).
    onComplete { x =>
      canvas.dispose()
      system.shutdown();
    }

  //  }

}