package breakybot

import akka.actor.Actor
import akka.event.{EventHandler => log}

package message.permission {
  case object Request
  case object Granted
}

package entity {
  import message.permission

  class PermissionBot extends Actor {
    def receive = {
      case permission.Request => self.reply_?( permission.Granted )
      case msg => log.error(this,"An unknown, unknown: %s".format(msg))
    }
  }
}
