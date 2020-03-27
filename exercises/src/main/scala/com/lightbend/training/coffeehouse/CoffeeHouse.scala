package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging}
import akka.actor.Props
import akka.actor.ActorRef
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

object CoffeeHouse {
  case object CreateGuest
  case class CreateGuest(favoriteCoffe : Coffee)
  def props(): Props = Props(new CoffeeHouse)
}

class CoffeeHouse extends Actor with ActorLogging {

  import CoffeeHouse._

  log.debug("CoffeeHouse Open!")

  private val finishCoffeeDuration: FiniteDuration = context.system.settings.config.getDuration("coffee-house.guest.finish-coffee-duration", TimeUnit.MILLISECONDS).millis

private val prepareCoffeeDuration: FiniteDuration = context.system.settings.config.getDuration("coffee-house.barista.prepare-coffee-duration", TimeUnit.MILLISECONDS).millis

  private val barista: ActorRef = createBarista
  private val waiter:ActorRef = createWaiter(barista)


  protected def createBarista(): ActorRef = {
    context.actorOf(Barista.props(prepareCoffeeDuration), "barista")
  }
  protected def createGuest(favoriteCoffee: Coffee): ActorRef = context.actorOf(Guest.props(waiter, favoriteCoffee, finishCoffeeDuration))

  protected def createWaiter(barista: ActorRef): ActorRef = context.actorOf(Waiter.props(barista), "waiter")
  override def receive: Receive = {
    case CreateGuest(favoriteCoffe) => createGuest(favoriteCoffe)
    case _ => sender() ! "coffee brewing"
  }
}
