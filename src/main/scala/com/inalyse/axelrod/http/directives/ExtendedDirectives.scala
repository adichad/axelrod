package com.inalyse.axelrod.http.directives

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{HttpResponse, RemoteAddress, StatusCodes}
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server._

/**
  * Created by adichad on 16/07/16.
  */
trait ExtendedDirectives extends Directives {


  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 = {
    mapResponseHeaders { headers =>
      `Access-Control-Allow-Origin`.* +:
        `Access-Control-Allow-Credentials`(true) +:
        `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With") +:
        headers
    }
  }

  //this handles preflight OPTIONS requests. TODO: see if can be done with rejection handler,
  //otherwise has to be under addAccessControlHeaders
  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).withHeaders(
      `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)
    )
    )
  }

  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  def extractClientIPOrUnknown: Directive1[RemoteAddress] =
    extractClientIP | headerValuePF { case _ ⇒ RemoteAddress.Unknown }


  def validateIP(r: Route)(f: RemoteAddress => Boolean): Route = {
    extractClientIPOrUnknown { ip=>
      validate(f(ip), "invalid request source") {
        r
      }
    }
  }
}
