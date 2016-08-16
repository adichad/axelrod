package com.inalyse.axelrod.server

import com.inalyse.axelrod.conf.Configured

/**
  * Created by adichad on 02/07/16.
  */
trait Server extends Configured with AutoCloseable {
  def bind(): Unit
}
