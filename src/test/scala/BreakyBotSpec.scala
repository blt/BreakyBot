package breakybot.spec

import org.scalatest.{Spec,BeforeAndAfterAll,BeforeAndAfterEach}
import akka.actor.Actor.actorOf
import akka.testkit.TestKit
import akka.util.duration._
import akka.actor.ActorRef

import breakybot.entity
import breakybot.message.permission

class BreakyBotSpec extends Spec with BeforeAndAfterAll with BeforeAndAfterEach with TestKit {
  private var machine:ActorRef = _

  override def beforeEach() = {
    machine = actorOf(new entity.BreakyBot(testActor, testActor)).start
  }
  override def afterEach() = {
    machine.stop
  }
  override def afterAll() = {
    stopTestActor
  }

  describe("BreakyBot") {
    it("should request permission to dance with a new partner") {
      machine ! "Edgar Allen Poe"
      expectMsg(50 millis, permission.Request)
      expectNoMsg(10 millis)
    }
    it("should brag to its diary when it can dance with a new partner") {
      machine ! "Oscar Wilde"
      expectMsg(50 millis) {
        case permission.Request => testActor.reply( permission.Granted )
        case _ => fail
      }
      expectMsg(0 millis, "Oscar Wilde wants to break dance with me!")
      expectNoMsg(5 millis)
    }
    it("should eventually lose its partner to the ravages of time") {
      machine ! "Roger B. Myerson"
      expectMsg(50 millis) {
        case permission.Request => testActor.reply( permission.Granted )
        case _ => fail
      }
      expectMsg(1 millis, "Roger B. Myerson wants to break dance with me!")
      expectMsg(15 millis, "Dancing with Roger B. Myerson was fun!")
      expectNoMsg(50 millis)
    }
  }

}
