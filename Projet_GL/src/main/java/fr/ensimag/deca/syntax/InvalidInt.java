package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an expression that should be an int (ie that can be
 * assigned), but is not.
 *
 * @author gl17 Clement
 * @date 01/01/2018
 */
public class InvalidInt extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidInt(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Integer number is too large";
    }
}
