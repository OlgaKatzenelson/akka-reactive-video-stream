package fuse.akkaStream

/**
 * Created by admin on 7/20/15.
 */
import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Local actor which listens on any free port
 */
class LocalActor extends Actor{
  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
  }
  override def receive: Receive = {

    case msg:String => {
      println("got message from remote" + msg)
    }
  }
}


object LocalActor {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem",config)
    val localActor = system.actorOf(Props[LocalActor], name="local")
  }


}
