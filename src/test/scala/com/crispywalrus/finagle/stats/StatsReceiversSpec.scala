package com.crispywalrus
package finagle
package stats

import org.scalatest._

class StatsReceiversSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  final val testport = 8225
  final val testhost = "localhost"

  override def beforeAll: Unit =
    TestStatsDServer.start(testport)

  override def afterAll: Unit = {
    TestStatsDServer.stop
    TestStatsDServer.extract
  }

  val testFormatter = new NameFormatter("")

  "StatsDStatsReceiver" should "construct with host and port" in {
    val rec = new StatsDStatsReceiver("google.com", 1258, testFormatter)
    rec.host should be("google.com")
    rec.port should be(1258)
  }
  it should "create a counter" in {
    val rec = new StatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.counter("bobs", "your", "uncle")
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("bobs.your.uncle:0|c")
  }
  it should "record a counter being incremented" in {
    val rec = new StatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.counter("bobs", "your", "uncle")
    c.incr(2)
    c.incr()
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(3)
    messages(0) should be("bobs.your.uncle:0|c")
    messages(1) should be("bobs.your.uncle:2|c")
    messages(2) should be("bobs.your.uncle:1|c")
  }
  it should "create a gauge" in {
    val rec = new StatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.addGauge("bobs", "your", "uncle")(230.0f)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("bobs.your.uncle:230|g")
  }
  it should "record a gauge" in {
    val rec = new StatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.addGauge("bobs", "your", "uncle") _
    c(2.0f)
    c(3.0f)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(2)
    messages(0) should be("bobs.your.uncle:2|g")
    messages(1) should be("bobs.your.uncle:3|g")
  }
  it should "create a stat" in {
    val rec = new StatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.stat("bobs", "your", "uncle")
    c.add(100)
    c.add(110)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(2)
    messages(0) should be("bobs.your.uncle:100.0|s")
    messages(1) should be("bobs.your.uncle:110.0|s")
  }

  "DogStatsDStatsReceiver" should "construct with host and port" in {
    val rec = new StatsDStatsReceiver("facebook.com", 3333, testFormatter)
    rec.host should be("facebook.com")
    rec.port should be(3333)
  }
  it should "create a counter" in {
    val rec = new DogStatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.counter("bobs", "your", "uncle")
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("bobs.your.uncle:0|c")
  }
  it should "record a counter being incremented" in {
    val rec = new DogStatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.counter("bobs", "your", "uncle")
    c.incr(2)
    c.incr()
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(3)
    messages(0) should be("bobs.your.uncle:0|c")
    messages(1) should be("bobs.your.uncle:2|c")
    messages(2) should be("bobs.your.uncle:1|c")
  }
  it should "create a gauge" in {
    val rec = new DogStatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.addGauge("bobs", "your", "uncle")(230.0f)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("bobs.your.uncle:230|g")
  }
  it should "record a gauge" in {
    val rec = new DogStatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.addGauge("bobs", "your", "uncle") _
    c(2.0f)
    c(3.0f)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(2)
    messages(0) should be("bobs.your.uncle:2|g")
    messages(1) should be("bobs.your.uncle:3|g")
  }
  it should "create a stat" in {
    val rec = new DogStatsDStatsReceiver(testhost, testport, testFormatter)
    val c   = rec.stat("bobs", "your", "uncle")
    c.add(100)
    c.add(110)
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(2)
    messages(0) should be("bobs.your.uncle:100.0|s")
    messages(1) should be("bobs.your.uncle:110.0|s")
  }

  "NameFormatter" should "append a prefix to all stat names" in {
    val rec = new StatsDStatsReceiver(testhost, testport, new NameFormatter("its.complicated"))
    val c   = rec.counter("bobs", "your", "uncle")
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("its.complicated.bobs.your.uncle:0|c")
  }
  it should "not put a period in if it's not needed" in {
    val rec = new StatsDStatsReceiver(testhost, testport, new NameFormatter("its.complicated."))
    val c   = rec.counter("bobs", "your", "uncle")
    Thread.sleep(20)
    val messages = TestStatsDServer.extract
    messages.size should be(1)
    messages.head should be("its.complicated.bobs.your.uncle:0|c")
  }
}
