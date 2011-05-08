package breakybot.spec

import org.scalatest.{Spec,BeforeAndAfterAll,BeforeAndAfterEach}
import akka.actor.Actor.actorOf
import akka.testkit.TestParts
import akka.util.duration._
import akka.actor.ActorRef

import breakybot.entity
import breakybot.message.permission

class BreakyBotSpec extends Spec with BeforeAndAfterAll with BeforeAndAfterEach {
  private var machine:ActorRef = _
  private val nanny:TestParts = new TestParts()
  private val diary:TestParts = new TestParts()

  override def beforeEach() = {
    machine = actorOf(new entity.BreakyBot(nanny.testActor, diary.testActor)).start
  }
  override def afterEach() = {
    machine.stop
  }
  override def afterAll() = {
    nanny.stopTestActor
    diary.stopTestActor
  }

  describe("BreakyBot") {
    it("should request permission to dance with a new partner") {
      machine ! "Edgar Allen Poe"
      nanny.expectMsg(50 millis, permission.Request)
      nanny.expectNoMsg(10 millis)
    }
    it("should brag to its diary when it can dance with a new partner") {
      machine ! "Oscar Wilde"
      nanny.expectMsg(50 millis) {
        case permission.Request => machine.!(permission.Granted)(Some(nanny.testActor))
        case _ => fail
      }
      diary.expectMsg(500 millis, "Oscar Wilde wants to break dance with me!")
      diary.expectNoMsg(40 millis)
    }
    it("should eventually lose its partner to the ravages of time") {
      machine ! "Roger B. Myerson"
      nanny.expectMsg(50 millis) {
        case permission.Request => machine.!(permission.Granted)(Some(nanny.testActor))
        case _ => fail
      }
      diary.expectMsg(5 millis, "Roger B. Myerson wants to break dance with me!")
      diary.expectMsg(2 second, "Dancing with Roger B. Myerson was fun!")
      diary.expectNoMsg(50 millis)
    }
  }
}
