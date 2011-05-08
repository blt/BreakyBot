package breakybot.spec

import org.scalatest.{Spec,BeforeAndAfterAll,BeforeAndAfterEach}
import akka.actor.Actor.actorOf
import akka.testkit.TestKit
import akka.util.duration._
import akka.actor.ActorRef

import breakybot.entity.PermissionBot
import breakybot.message.permission

class PermissionBotSpec extends Spec with BeforeAndAfterAll with BeforeAndAfterEach with TestKit {
  private var machine:ActorRef = _

  override def beforeEach() = {
    machine = actorOf[PermissionBot].start
  }
  override def afterEach() = {
    machine.stop
  }
  override def afterAll() = {
    stopTestActor
  }

  describe("PermissionBot") {
    it("should always grant requests") {
      machine ! permission.Request
      expectMsg(50 millis, permission.Granted)
      expectNoMsg(10 millis)
    }
  }

}
