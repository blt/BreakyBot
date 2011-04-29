package breakybot

import akka.actor.Actor
import akka.event.{EventHandler => log}

class BreakyBot extends Actor {
  throw new RuntimeException("No one has taught me to dance. :(")

  def receive = {
    case _ => self.reply_?("What if my friend jumps off the Brooklyn Bridge?")
  }
}
