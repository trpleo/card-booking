package framework

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.scalatest.Assertion

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class World {

  case class RequestResponse[Request, Response](serviceCall: ServiceCall[Request, Response], request: Request, response: Either[Throwable, Response])

  def updateWorld[Request, Response](implicit srvCall: ServiceCall[Request, Response], request: Request = NotUsed): Future[Response] = {

    val response = srvCall.invoke(request)

    response.onComplete {
      case Success(response) => history = history.:+(RequestResponse(srvCall, request, Right(response)))
      case Failure(t) => history = history.:+(RequestResponse(srvCall, request, Left(t)))
    }

    //    history.zipWithIndex.map { case (data, cnt) => println(s"== HISTORY ==>>>> $cnt: [$data]") }

    response
  }

  def checkLastHistoryRegistry[Response](check: RequestResponse[_, _] => Assertion) = {
    history.lastOption match {
      case Some(rr) => check(rr)
      case None => org.scalatest.Assertions.fail("There's no data in World's history.")
    }
  }

  // WARNING: immutable.List is thread safe by default. However parallel execution can
  //          cause still problems, since there can be race condition between parallelly
  //          executed assignments.
  //          See: AsyncWordSpec which is used in the tests.
  var history = List[RequestResponse[_, _]]()

  println(s"===>>> World is created with empty state. [${history.size}]")
}
