package PCF.Tests

import PCF.Interpreter.Interpreter
import PCF.Parser.AST._
import PCF.Parser.Parser
import org.scalatest.{ShouldMatchers, FlatSpec}

/**
 * Created by nperez on 11/10/15.
 */
class RECParserTest extends FlatSpec with ShouldMatchers {
  "Parser" should "return ERROR when REC has not body" in {
    val sourceCode = "rec x -> "

    val ast = Parser.parseStr(sourceCode)

    ast should be (ERROR("Expecting REC body"))
  }

  it should "return REC TERM when body is present" in {
    val sourceCode = "rec x -> a"

    val ast = Parser.parseStr(sourceCode)

    ast should be(REC("x", ID("a")))
  }

  it should "return ERROR when ARROW is missing" in {
    val sourceCode = "rec x b a"

    val ast = Parser.parseStr(sourceCode)

    ast should be (ERROR("Expected '->' after REC"))
  }


  it should "return ERROR when ID is missing" in {
    val sourceCode = "rec -> b"

    val ast = Parser.parseStr(sourceCode)

    ast should be (ERROR("Expecting identifier after REC"))
  }
}

class InterpreterTest extends FlatSpec with ShouldMatchers {
  "Interpreter" should "return ERROR when unbound indentifier" in {

    val ast = ID("x")

    val result = Interpreter.eval(ast)

    result should be (ERROR("Unbound identifier"))
  }

  it should "return ERROR when REC has not FUN body" in {

    Interpreter.eval(REC("f", APP(ID("x"), ID("x")))) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", BOOL(true))) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", ERROR("asdf"))) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", IF(ID("a"),ID("a"),ID("a")))) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", ISZERO())) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", NUM(5))) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", PRED())) should be (ERROR("Expecting FUN body"))
    Interpreter.eval(REC("f", SUCC())) should be (ERROR("Expecting FUN body"))
  }

  it should "return NUM when apply  NUM" in {
    val ast =  NUM(5)

    Interpreter.eval(ast) should be (NUM(5))
  }

  it should "return 6 when apply SUCC NUM(5)" in {
    val ast = APP(SUCC(), NUM(5))

    Interpreter.eval(ast) should be (NUM(6))
  }

  it should "return 5 when apply PRED NUM(6)" in {
    val ast = APP(PRED(), NUM(6))

    Interpreter.eval(ast) should be (NUM(5))
  }
}
