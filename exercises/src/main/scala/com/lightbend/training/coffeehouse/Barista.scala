package com.lightbend.training.coffeehouse

import akka.actor.{ActorRef, ActorLogging, Actor, Props}
import scala.concurrent.duration.FiniteDuration

class Barista(prepareCoffeeDuration: FiniteDuration) extends Actor with ActorLogging{
  import Barista._

  def receive: Actor.Receive = {
    case PrepareCoffee(coffee, guest) =>
      busy(prepareCoffeeDuration)
        sender() ! CoffeePrepared(coffee, guest)
  }
}

object Barista {
  case class PrepareCoffee(coffee: Coffee, guest: ActorRef)
  case class CoffeePrepared(coffee: Coffee, guest: ActorRef)

  def props(prepareCoffeeDuration: FiniteDuration): Props = Props(new Barista(prepareCoffeeDuration))

}
