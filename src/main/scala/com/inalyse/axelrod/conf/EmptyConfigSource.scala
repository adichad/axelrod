package com.inalyse.axelrod.conf

import com.netflix.config.{DynamicConfiguration, DynamicPropertyFactory, FixedDelayPollingScheduler}
import com.typesafe.config.{Config, ConfigFactory}
import grizzled.slf4j.Logging

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
