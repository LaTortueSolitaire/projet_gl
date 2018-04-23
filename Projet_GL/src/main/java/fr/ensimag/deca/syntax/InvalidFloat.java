package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an expression that should be a float (ie that can be
 * assigned), but is not.
 *
 * @author gl17 Clement
 * @date 01/01/2018
 */
public class InvalidFloat extends DecaRecognitionException {

	private String message;
    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidFloat(DecaParser recognizer, ParserRuleContext ctx, String message) {
        super(recognizer, ctx);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
