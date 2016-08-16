package com.inalyse.axelrod.hg

import com.inalyse.axelrod.conf.Configured
import org.hypergraphdb.storage.HGStoreImplementation
import org.hypergraphdb.{HGConfiguration, HGEnvironment, HyperGraph}

/**
  * Created by adichad on 15/08/16.
  */
object HyperGraphDB extends Configured {
  override protected[this] val scope = "hg"
  private val c = new HGConfiguration()
  c.setTransactional(boolean("transactional"))
  c.setStoreImplementation(Class.forName(string("storage-impl")).getConstructor().newInstance().asInstanceOf[HGStoreImplementation])
  override implicit val hgdb: HyperGraph = HGEnvironment.get(string("path.data"), c)
  info("hypergraph-db instantiated")
}
