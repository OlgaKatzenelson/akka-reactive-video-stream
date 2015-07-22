package fuse.akkaStream

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Broadcast, Flow, FlowGraph, RunnableFlow, Sink, Source}
import org.bytedeco.javacpp.opencv_core.{IplImage, _}
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade
import org.bytedeco.javacv.{CanvasFrame, Frame, OpenCVFrameConverter, OpenCVFrameGrabber}



import org.bytedeco.javacpp.opencv_highgui._
import org.bytedeco.javacpp.opencv_objdetect._

object FramesSource3 extends App {

  //  def main(args: Array[String]): Unit = {
  //    run
  //  }
  //
  //  def run: scala.Unit = {
  implicit val system = ActorSystem("Sys")

  implicit val materializer = ActorFlowMaterializer()
  val CASCADE_FILE ="data/haarcascade_frontalface_default.xml";

  val canvas = new CanvasFrame("My Video", 1);
  val grabber: OpenCVFrameGrabber = new OpenCVFrameGrabber(0)
  grabber.start()

  val source: Source[Frame, Unit] =
    Source(() => Iterator.continually(grabber.grab())) ;

  //val sink = Sink.foreach { f: Frame => canvas.showImage(f) }
  val sink = Sink.foreach { f: Frame => canvas.showImage(f) }

  def convertGrayScale(frame: Frame): Frame = {
    val x: OpenCVFrameConverter.ToIplImage = new OpenCVFrameConverter.ToIplImage();
    val image: IplImage = x.convert(frame);




    val imageBW: IplImage = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
    cvCvtColor(image, imageBW, CV_BGR2GRAY);

//    val storage:CvMemStorage = new CvMemStorage;
//    val cascade:CvHaarClassifierCascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
//    val faces:CvSeq = cvHaarDetectObjects(imageBW, cascade, storage);

//    for( i <- 0 to faces.total){
//      val r:CvRect = new CvRect(cvGetSeqElem(faces, i));
//      cvRectangle(image, cvPoint(r.x(), r.y()), cvPoint(r.x() + r.width(), r.y() + r.height()), new CvScalar, 5, CV_AA, 0);
//    }

    x.convert(imageBW)

//    x.convert(image)

  }

  val flow = Flow[Frame].map { f => convertGrayScale(f) }

  
  
  val g : RunnableFlow[Unit] = FlowGraph.closed() { implicit b =>
    import FlowGraph.Implicits._

    val bcast = b.add(Broadcast[Frame](1))
    source ~> bcast.in
    bcast.out(0) ~> flow ~> sink
  }
  
  g.run()
  //g.onComplet
  
  
  //  source.runWith(sink).
  //    onComplete { x =>
  //      canvas.dispose()
  //      system.shutdown();
  //    }

  //  }

}