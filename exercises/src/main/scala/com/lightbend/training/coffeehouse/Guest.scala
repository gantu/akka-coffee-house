package com.lightbend.training.coffeehouse

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.ActorRef
import scala.concurrent.duration.FiniteDuration
import akka.actor.Timers

class Guest(
  waiter: ActorRef,
  favoriteCoffee: Coffee,
  finishCoffeeDuration: FiniteDuration) extends Actor with ActorLogging with Timers{
  import Guest._

  private var coffeeCount: Int = 0

  orderCoffee

  def receive: Actor.Receive = {
    case Waiter.CoffeeServed(coffee) =>
       coffeeCount += 1
         log.info(s"Enjoying $coffeeCount yummy $coffee")
         timers.startSingleTimer("coffee-finished", CoffeeFinished,finishCoffeeDuration)

    case CoffeeFinished => orderCoffee
  }

  private def orderCoffee = waiter ! Waiter.ServeCoffee(favoriteCoffee)
}

object Guest {
  case object CoffeeFinished
  def props(
    waiter: ActorRef,
    favoriteCoffee: Coffee,
    finishCoffeeDuration: FiniteDuration) : Props = Props(new Guest(waiter, favoriteCoffee, finishCoffeeDuration))
}
