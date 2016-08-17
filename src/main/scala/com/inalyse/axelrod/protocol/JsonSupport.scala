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
