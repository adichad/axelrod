package com.inalyse.axelrod.server

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.inalyse.axelrod.conf.Configured

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by adichad on 12/08/16.
  */
object AkkaContext extends Configured {
  override protected[this] val scope = "akka"

  override implicit val actorSystem: ActorSystem = ActorSystem(string("name"), conf())
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  val noop = actorSystem.actorOf(Props.empty)

}
