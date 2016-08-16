package com.inalyse.axelrod.http.router

import akka.http.scaladsl.model.StatusCodes._
import com.github.swagger.akka.model.Info
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}
import grizzled.slf4j.Logging

import scala.reflect.runtime.{universe => ru}

/**
  * Created by adichad on 05/07/16.
  */
case class SwaggerDocs(scope: String)
                  extends Router with SwaggerHttpService with HasActorSystem {


  override val apiTypes = Seq(ru.typeOf[Hello])
  override val info = Info(version = "1.0")
  override def route =
    routes ~
      pathPrefix("swagger") {
        getFromResourceDirectory("swagger") ~
          pathEndOrSingleSlash {
            get(redirect("/swagger/index.html?url=/api-docs/swagger.json", PermanentRedirect))
          }
      } ~
      pathEndOrSingleSlash {
        getFromResourceDirectory("swagger") ~
          get(redirect("/swagger/index.html?url=/api-docs/swagger.json", PermanentRedirect))
      }
}