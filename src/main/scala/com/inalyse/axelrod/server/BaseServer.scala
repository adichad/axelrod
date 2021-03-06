/*
 * Copyright 2016 Aditya Varun Chadha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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