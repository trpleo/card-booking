package framework

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.scalatest.Assertion
import org.scalatest.Assertions.fail

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class World {

  case class RequestResponse[Request, Response](serviceCall: ServiceCall[Request, Response], request: Request, response: Either[Throwable, Response])

  def updateWorld[Request, Response](implicit srvCall: ServiceCall[Request, Response], request: Request = NotUsed): Future[Response] = {

    val response = srvCall.invoke(request)

    response.onComplete {
      case Success(response) => history = history :+ RequestResponse(srvCall, request, Right(response))
      case Failure(t) => history = history :+ RequestResponse(srvCall, request, Left(t))
    }

    //    history.zipWithIndex.map { case (data, cnt) => println(s"== HISTORY ==>>>> $cnt: [$data]") }

    response
  }

  def defaultFallback[Response](rr: RequestResponse[_, Response]): PartialFunction[scala.Either[scala.Throwable, Response], Assertion] = {
    case Right(r) => fail(s"No error, but unexpected result. Value: Right($r)")
    case Left(t) => fail(s"Error. Func: [${rr.serviceCall}] Rq: [${rr.request}] Cause: [${t.getMessage}]")
  }

  def checkHistoryRegistry[Response](check: RequestResponse[_, Response] => Assertion)(nth: Int = 1): Assertion = {
    history.takeRight(nth).headOption match {
      case Some(rr) => check(rr.asInstanceOf[RequestResponse[_, Response]])
      case None => fail("There's no data in World's history.")
    }
  }

  def checkHistoryRegistryWithDefaultFallback[Response](pf: PartialFunction[Either[Throwable, Response], Assertion], nth: Int = 1): Assertion = {
    checkHistoryRegistry[Response] { rr => (pf orElse defaultFallback(rr))(rr.response) }(nth)
  }

  // WARNING: immutable.List is thread safe by default. However parallel execution can
  //          cause still problems, since there can be race condition between parallelly
  //          executed assignments.
  //          See: AsyncWordSpec which is used in the tests.
  var history = List[RequestResponse[_, _]]()

  println(s"===>>> World is created with empty state. [${history.size}]")
}
