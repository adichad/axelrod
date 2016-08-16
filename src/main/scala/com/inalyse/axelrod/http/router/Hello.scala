package com.inalyse.axelrod.http.router

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import com.inalyse.axelrod.protocol.JsonSupport
import com.inalyse.axelrod.protocol.response.HelloMessage
import io.swagger.annotations.{Api, ApiOperation}

/**
  * Created by adichad on 05/07/16.
  */
@Path("/hello")
@Api(value = "/hello", produces = "application/json")
case class Hello(scope: String) extends Router with JsonSupport {

  @ApiOperation(value = "Say Hello", nickname = "hello", httpMethod = "GET")
  override def route: Route =
    path("hello") {
      get {
        extractRequestContext { reqCtx =>
          extractRequest { req =>
            parameters('name?"") { (name) =>
              info("hello "+name)
              complete(
                OK, HelloMessage(name))
            }
          }
        }
      }
    }
}
