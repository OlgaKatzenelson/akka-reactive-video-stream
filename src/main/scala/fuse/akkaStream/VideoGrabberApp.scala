package fuse.akkaStream

import javax.swing.JFrame._

import org.bytedeco.javacv.{ CanvasFrame, OpenCVFrameGrabber }

/**
 * Created by admin on 7/18/15.
 */
object VideoGrabberApp extends App {

  val grabber = new OpenCVFrameGrabber("data/data.mp4");
  grabber.start()

  // Create image window named "My Image".
  //
  // Note that you need to indicate to CanvasFrame not to apply gamma correction,
  // by setting gamma to 1, otherwise the image will not look correct.
  val canvas = new CanvasFrame("My Video", 1)

  // Request closing of the application when the image window is closed
  canvas.setDefaultCloseOperation(EXIT_ON_CLOSE)

  try {
    while (true) {
      canvas.showImage(grabber.grab())
    }
  } catch {
    case _ => println("End of stream")
  }
}