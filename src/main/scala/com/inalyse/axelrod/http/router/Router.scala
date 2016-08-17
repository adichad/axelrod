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

import akka.http.scaladsl.server.{Directives, Route}
import com.inalyse.axelrod.conf.Configured
import com.inalyse.axelrod.http.directives.ExtendedDirectives

/**
  * Created by adichad on 03/07/16.
  */
trait Router extends Configured with Directives with ExtendedDirectives {
  def route: Route
}
