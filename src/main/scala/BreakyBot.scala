package breakybot

import akka.actor.{Actor,ActorRef,FSM}
import akka.event.{EventHandler => log}
import akka.util.duration._

package fsm.breakybot {
  sealed trait State
  case object NoDance extends State
  case object MuchDance extends State
  case object LosePartner extends State
}

package message.permission {
  case object Request
  case object Granted
}

package entity {
  import fsm.breakybot.{NoDance, MuchDance, LosePartner}
  import message.permission

  class PermissionBot extends Actor {
    def receive = {
      case permission.Request => self.reply_?( permission.Granted )
      case msg => log.error(this,"An unknown, unknown: %s".format(msg))
    }
  }

  class Diary extends Actor {
    def receive = {
      case msg => log.info(this, "Dear diary: %s".format(msg))
    }
  }

  class BreakyBot(nanny:ActorRef,diary:ActorRef) extends Actor with FSM[fsm.breakybot.State,Option[String]] {
    import FSM._

    startWith(NoDance, None)

    when(NoDance) {
      case Event(permission.Granted, Some(partner)) =>
        diary ! "%s wants to break dance with me!".format(partner)
        goto(MuchDance) using Some(partner)
      case Event(partner:String, None) =>
        nanny ! permission.Request
        stay using Some(partner)
      case Event(unknown, state) =>
        log.error(this,"I don't understand this: %s".format(unknown))
        stay using state
    }

    when(MuchDance, stateTimeout = 1 second) {
      case Event(StateTimeout, Some(partner)) =>
        diary ! "Dancing with %s was fun!".format(partner)
        goto(LosePartner) using Some(partner)
      case Event(unknown, state) =>
        log.error(this,"I don't understand this: %s".format(unknown))
        stay using state
    }

    when(LosePartner, stateTimeout = 0 millis) {
      case Ev(StateTimeout) =>
        goto(NoDance) using None
      case Event(unknown, state) =>
        log.error(this,"I don't understand this: %s".format(unknown))
        stay using state
    }

    initialize
  }
}
