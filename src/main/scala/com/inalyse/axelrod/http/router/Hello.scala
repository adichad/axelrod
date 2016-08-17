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
