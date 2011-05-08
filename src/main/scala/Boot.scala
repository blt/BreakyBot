package breakybot

import akka.actor.Actor.actorOf
import akka.config.Supervision._
import akka.actor.Supervisor

class Boot {
  val strategy = OneForOneStrategy(List(classOf[Exception]), 3, 1000)
  val supervisor = Supervisor(SupervisorConfig(strategy, Nil))

  val permission = actorOf[entity.PermissionBot].start
  val diary = actorOf[entity.Diary].start

  supervisor.link(permission)
  supervisor.link(diary)
  supervisor.link(actorOf(new entity.BreakyBot(permission, diary)).start)
}
