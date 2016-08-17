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

package com.inalyse.axelrod

import java.io.{File, IOException, PrintWriter}
import java.lang.management.ManagementFactory

import com.inalyse.axelrod.conf.Configured
import com.inalyse.axelrod.server.Server

import scala.collection.JavaConversions._

/**
  * Created by adichad on 02/07/16.
  */
object Launcher extends App with Configured {
  override val scope = ""
  try {
    info(sysprop("component.name"))
    info(s"args: ${args.toList}")
    if(args.length > 0)
      println("usage: java -jar ")
    else {
      info("Log path current: " + sysprop("log.path.current"))
      info("Log path archive: " + sysprop("log.path.archive"))

      writePID(sysprop("daemon.pidfile"))
      if (boolean("sysout.detach")) System.out.close()
      if (boolean("syserr.detach")) System.err.close()

      val servers = configureds[Server]("servers").values.toSeq

      closeOnExit(servers)
      servers foreach (_.bind())
    }
  } catch {
    case e: Throwable =>
      error("fatal", e)
      throw e
  }

  private[this] def writePID(destPath: String) = {
    def getPid(fallback: String) = {
      val jvmName = ManagementFactory.getRuntimeMXBean.getName
      val index = jvmName indexOf '@'
      if (index > 0) {
        try {
          jvmName.substring(0, index).toLong.toString
        } catch {
          case e: NumberFormatException ⇒ fallback
        }
      } else fallback
    }

    val pidFile = new File(destPath)
    if (pidFile.createNewFile) {
      val pid = getPid("unknown-pid")
      (new PrintWriter(pidFile) append pid).close()
      pidFile.deleteOnExit()
      info(s"pid file [pid]: $destPath [$pid]")
      pid
    } else {
      throw new IOException(s"pid file already exists at: $destPath")
    }
  }

  private[this] def closeOnExit(closeables: Seq[AutoCloseable]) = {
    Runtime.getRuntime addShutdownHook new Thread {
      override def run() = {
        try {
          info(string("component.name")+" shutdown initiated")
          closeables.foreach(_.close)
          actorSystem.terminate()
          info("actor system terminated")
          info(string("component.name")+" shutdown complete")
        } catch {
          case e: Throwable => error("shutdown hook failure", e)
        }
      }
    }
  }


}
