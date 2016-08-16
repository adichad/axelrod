package com.inalyse.axelrod.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.inalyse.axelrod.protocol.response._
import spray.json.DefaultJsonProtocol

/**
  * Created by adichad on 20/07/16.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val helloMessage = jsonFormat1(HelloMessage)
}
