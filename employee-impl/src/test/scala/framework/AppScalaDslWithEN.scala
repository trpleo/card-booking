package framework

import scala.language.{ implicitConversions, postfixOps }
import cucumber.api.scala.{ EN, ScalaDsl }

class AppScalaDslWithEN extends ScalaDsl with EN {
  trait GherkinGrammarExtension { val postfix: String }
  object API extends GherkinGrammarExtension { val postfix = "api" }
  object WEB extends GherkinGrammarExtension { val postfix = "web" }
  object MOB extends GherkinGrammarExtension { val postfix = "mob" }

  final class StepCustom(name: String, postfix: String) {

    private val postfixChLst = List(s"\\s*\\| .*$postfix.*").flatten.reverse
    private def extend(r: String) = {
      val chs = List(r).flatten.reverse
      val ret = (chs.head == '$') match {
        case true => chs.head :: postfixChLst ::: chs.tail
        case false => postfixChLst ::: chs.tail
      }
      val result = ret.reverse.mkString
      //      log.debug(s"Custom gherkin pattern was applied [$result]")
      result
    }

    def apply(regex: String): StepBody = new StepBody(name, extend(regex))
  }

  val GivenAPI = new StepCustom("GivenAPI", API.postfix)
  val WhenAPI = new StepCustom("WhenAPI", API.postfix)
  val ThenAPI = new StepCustom("ThenAPI", API.postfix)
}
