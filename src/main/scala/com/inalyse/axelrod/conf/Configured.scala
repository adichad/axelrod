package com.inalyse.axelrod.conf

import java.util.{Map, Properties}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.inalyse.axelrod.hg.HyperGraphDB
import com.inalyse.axelrod.server.AkkaContext
import com.typesafe.config.{Config, ConfigFactory}
import grizzled.slf4j.Logging
import org.hypergraphdb.HyperGraph

import scala.collection.JavaConversions.{asScalaBuffer, mapAsScalaMap, _}
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.TimeUnit

/**
  * Created by adichad on 02/07/16.
  */

object Configured extends Configured {

  override protected[this] val scope = ""
  private[this] val originalConfig =
    configure("environment", "application", "environment_defaults", "application_defaults")
  private var config = originalConfig
  private[this] val staticProperties = strings("system.properties.static")
  configureSystem(staticProperties: _*)
  configureSystemDynamic()
  // logging configured at this point
  private[this] val configSource = configured[ConfigSource]("config.source")
  configSource.initConfig()

  def apply(config: Config): Unit = {
    val c = ConfigFactory.load(config.withFallback(originalConfig))
    synchronized(this.config = c)
    configureSystemDynamic()
  }

  private def configureSystemDynamic(): Unit = {
    val dynamicProperties = strings("system.properties.dynamic")
    dynamicProperties.removeAll(staticProperties)
    configureSystem(dynamicProperties: _*)
  }

  private def configure(resources: String*) =
    ConfigFactory.load(
      resources
        .map(ConfigFactory.parseResourcesAnySyntax)
        .reduceLeft(_ withFallback _)
        .withFallback(ConfigFactory.systemEnvironment()))

  private def configureSystem(propertyNames: String*) =
    propertyNames.foreach{p =>
      System.setProperty(p, string(p))
    }

}

trait Configured extends Logging {
  import Configured.config
  protected[this] val scope: String

  private[this] final def path(part: String) =
    if(scope.isEmpty) part else if(part.isEmpty) scope else s"$scope.$part"

  private[this] final def path(prefix: String, part: String) =
    if(prefix.isEmpty) part else if(part.isEmpty) prefix else s"$prefix.$part"


  protected[this] final def configured[T <: Configured](part: String): T =
    Class.forName(string(s"$part.type")).getConstructor(classOf[String]).newInstance(path(part)).asInstanceOf[T]

  protected[this] final def configureds[T <: Configured](part: String): Map[String, T] =
    (config getAnyRef path(part)).asInstanceOf[Map[String, AnyRef]]
      .map { x =>
        val p = path(path(part), x._1)
        x._1 -> Class.forName(string(s"${path(part, x._1)}.type")).getConstructor(classOf[String]).newInstance(p).asInstanceOf[T]
      }.toMap[String, T]

  protected[this] final def keys(part: String) = (config getAnyRef path(part)).asInstanceOf[Map[String, Any]].keySet
  protected[this] final def vals[T](part: String) = (config getAnyRef path(part)).asInstanceOf[Map[String, T]].values

  protected[this] final def size(part: String) = config getBytes path(part)
  protected[this] final def boolean(part: String) = config getBoolean path(part)
  protected[this] final def int(part: String) = config getInt path(part)
  protected[this] final def long(part: String) = config getLong path(part)

  protected[this] final def double(part: String) = config getDouble path(part)
  protected[this] final def string(part: String) = config getString path(part)
  protected[this] final def duration(part: String, unit: TimeUnit) = config getDuration(path(part), unit)
  protected[this] final def conf(part: String = "") = config getConfig path(part)

  protected[this] final def sizes(part: String) = config getBytes path(part)
  protected[this] final def booleans(part: String) = config getBooleanList path(part)
  protected[this] final def ints(part: String) = config getIntList path(part)
  protected[this] final def longs(part: String) = config getLongList path(part)
  protected[this] final def doubles(part: String) = config getDoubleList path(part)
  protected[this] final def strings(part: String) = config getStringList path(part)
  protected[this] final def durations(part: String, unit: TimeUnit) = config getDurationList(path(part), unit)
  protected[this] final def confs(part: String) = config getConfigList path(part)

  protected[this] final def props(part: String): Properties = {
    val p = new Properties
    val c = conf(part)
    for(e <- c.entrySet())
      p.setProperty(e.getKey, c getString e.getKey)
    p
  }

  protected[this] final def sysprop(part: String) =
    System.getProperty(path(part))

  implicit val actorSystem: ActorSystem = AkkaContext.actorSystem
  implicit val materializer: ActorMaterializer = AkkaContext.materializer
  implicit val executionContext: ExecutionContextExecutor = AkkaContext.executionContext
  implicit val hgdb: HyperGraph = HyperGraphDB.hgdb
  def reactor = AkkaContext.noop
}
