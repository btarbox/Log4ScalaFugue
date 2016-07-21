package org.log4jfugue


import scala.concurrent.Await
import scala.language.postfixOps
import scala.concurrent.duration._
import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, Suite}

trait BaseAkkaSpec extends BeforeAndAfterAll { this: Suite =>
  implicit val system = ActorSystem(this.getClass.getSimpleName.replace("$", "_"))

  override protected def afterAll(): Unit = {
    super.afterAll()
    Await.result(system.terminate(), 5 seconds)
  }
}

