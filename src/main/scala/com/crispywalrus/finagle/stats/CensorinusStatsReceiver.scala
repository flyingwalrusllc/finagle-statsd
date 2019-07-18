package com.crispywalrus
package finagle
package stats

import com.twitter.finagle.stats._
import com.twitter.app.GlobalFlag
import github.gphat.censorinus._

/**
 * wrap up using censorinus to forward metrics to either statsd or
 * dogstatsd based on flags and toggles.
 *
 *  based on the usage of GlobalFlag the flags are
 *  - com.crispywalrus.finagle.stats.hostname, defaults to localhost
 *  - com.crispywalrus.finagle.stats.port, defaults to 8125
 *  - com.crispywalrus.finagle.stats.implementation statsd/dogstatsd defaults to statsd
 */
class CensorinusStatsReceiver
    extends StatsReceiver {

  object hostname extends GlobalFlag[String]("localhost", "statsd hostname")
  object port extends GlobalFlag[Int](8125, "statsd port")
  object prefix extends GlobalFlag[String]("","a dotted prefix to prepend to stats names (to namespace default stats)")

  object implementation extends GlobalFlag[String]("statsd", "type of metrics gathering server [statsd,dogstatsd]")

  lazy val impl: StatsReceiver = implementation() match {
    case "dogstatsd" => new DogStatsDStatsReceiver(hostname(), port(),new NameFormatter(prefix()))
    case "statsd" => new StatsDStatsReceiver(hostname(), port(),new NameFormatter(prefix()))
    case iname => throw new IllegalArgumentException(s"$iname is invalid driver name")
  }

  override val repr: AnyRef = this

  override def counter(verbosity: Verbosity, name: String*) = impl.counter(name: _*)

  override def addGauge(verbosity: Verbosity, name: String*)(f: => Float) = impl.addGauge(name: _*)(f)

  override def stat(verbosity: Verbosity, name: String*) = impl.stat(name: _*)

}


/**
  * forward all stats to statsd
  */
class StatsDStatsReceiver(
  val host: String,
  val port: Int,
  formatter: NameFormatter
) extends StatsReceiver {

  val c = new StatsDClient(host, port)

  override val repr: AnyRef = this

  override def counter(verbosity: Verbosity, name: String*) = new Counter {
    val counterName = formatter.formatName(name)
    c.counter(counterName, value = 0)
    override def incr(delta: Long): Unit = {
      c.increment(counterName, delta.toDouble)
    }
  }

  override def stat(verbosity: Verbosity, name: String*) = new Stat {
    val statName = formatter.formatName(name)
    def add(value: Float) = {
      c.set(name = statName, value = value.toString)
    }
  }

  override def addGauge(verbosity: Verbosity, name: String*)(f: => Float) = new Gauge {
    val gaugeName = formatter.formatName(name)
    def remove(): Unit = {
    }
    c.gauge(name = gaugeName, value = f.toDouble)
  }

}

class DogStatsDStatsReceiver(
  host: String,
  port: Int,
  formatter: NameFormatter
) extends StatsReceiver {

  override val repr: AnyRef = this

  val c = new DogStatsDClient(host, port)

  override def counter(verbosity: Verbosity, name: String*) = new Counter {
    val counterName = formatter.formatName(name)
    c.counter(counterName, value = 0)
    override def incr(delta: Long): Unit = {
      c.increment(formatter.formatName(name), delta.toDouble)
    }
  }

  override def stat(verbosity: Verbosity, name: String*) = new Stat {
    val statName = formatter.formatName(name)
    def add(value: Float) = {
      c.set(name = statName, value = value.toString)
    }
  }

  override def addGauge(verbosity: Verbosity, name: String*)(f: => Float) = new Gauge {
    val gaugeName = formatter.formatName(name)
    def remove(): Unit = {
    }
    c.gauge(name = gaugeName, value = f.toDouble)
  }
}

class NameFormatter(prefix: String) {
  val p =
    if( prefix.endsWith("."))
      prefix
    else
      if( !prefix.trim.isEmpty )
        prefix+"."
      else
        prefix

  def formatName(description: Seq[String]) =
    p + (description map { _.replaceAll("\\.", "-") } map { _.replaceAll(":", ";") } mkString ".")
}


