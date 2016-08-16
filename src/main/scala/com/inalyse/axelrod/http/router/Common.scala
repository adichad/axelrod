package com.inalyse.axelrod.http.router

import akka.http.scaladsl.model.RemoteAddress

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

/**
  * Created by adichad on 03/07/16.
  */

class Common(val scope: String) extends Router {

  info(s"router ${string("name")} instantiated")
  override def route =

    corsHandler {
      withRequestTimeout(Duration(string("default-timeout"))) {
        withPrecompressedMediaTypeSupport {
          validateIP {
            encodeResponse {
              configureds[Router]("routes").values().map(_.route).reduceLeft(_ ~ _)
            }
          }(f = (ip: RemoteAddress)=>true)
        }
      }
    }

}
