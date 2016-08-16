package com.inalyse.axelrod.server


import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import com.inalyse.axelrod.http.router.Router

import scala.collection.JavaConversions._

/**
  * Created by adichad on 02/07/16.
  */
class BaseServer(val scope: String) extends Server with Directives {

  lazy val bindingFuture =
    Http().bindAndHandle(
      configureds[Router]("routes").values().map(_.route).reduceLeft(_ ~ _),
      interface = string("host"), port = int("port")
    )

  override def bind(): Unit = {
    bindingFuture.onSuccess {
      case r => info(s"${string("name")} bound: $r")
    }
    bindingFuture.onFailure {
      case e: Throwable =>
        error(s"error binding ${string("name")}", e)
        throw e
    }
  }

  override def close(): Unit = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete { _ =>
        info(s"${string("name")} unbound")
      }
    info(s"${string("name")} unbinding")
  }

}