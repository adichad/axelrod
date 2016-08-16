package com.inalyse.axelrod.http.router

import akka.http.scaladsl.server.{Directives, Route}
import com.inalyse.axelrod.conf.Configured
import com.inalyse.axelrod.http.directives.ExtendedDirectives

/**
  * Created by adichad on 03/07/16.
  */
trait Router extends Configured with Directives with ExtendedDirectives {
  def route: Route
}
