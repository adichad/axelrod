package com.inalyse.axelrod.conf

import com.netflix.config.sources.TypesafeConfigurationSource

/**
  * Created by adichad on 12/08/16.
  */
trait ConfigSource extends TypesafeConfigurationSource with Configured {
  def initConfig(): Unit
}