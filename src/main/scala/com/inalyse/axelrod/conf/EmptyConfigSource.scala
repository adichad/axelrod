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

package com.inalyse.axelrod.conf

import com.netflix.config.{DynamicConfiguration, DynamicPropertyFactory, FixedDelayPollingScheduler}
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by adichad on 12/08/16.
  */
class EmptyConfigSource(val scope: String) extends ConfigSource {

  override def config: Config = {
    val e = ConfigFactory.empty()
    Configured(e)
    info(s"config received: $e")
    e
  }

  override def initConfig() = {
    DynamicPropertyFactory.initWithConfigurationSource(
      new DynamicConfiguration(
        this,
        new FixedDelayPollingScheduler(int("initial-delay"), int("delay"), false)
      )
    )
    config
  }

}
