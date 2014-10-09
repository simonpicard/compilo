import java.util.regex.PatternSyntaxException;
import java.util.HashMap;

%%// Options of the scanner

%class LexicalAnalyzer	//Name
%unicode						//Use unicode
%line							//Use line counter (yyline variable)
%column						//Use character counter by line (yycolumn variable)
%function nextToken
%type Symbol
%yylexthrow PatternSyntaxException


%{
    private static HashMap<Integer, Symbol> identifiersMap = new HashMap<Integer, Symbol>();
    public static HashMap<Integer, Symbol> getIdentifiersMap() {
        return identifiersMap;
    }
%}

%eofval{
	return new Symbol(LexicalUnit.END_OF_STREAM,yyline, yycolumn);
%eofval}

//Extended Regular Expressions

EndOfLine = \r|\n|\r\n

Comment = #.*{EndOfLine}
Circumflex = \^
Percentage = %
Tilde = \~
Ampersand = &
Pipe = \|
Dollar = \$
DubbleGreater = >>
DubbleLower = <<
DubbleEqual = ==
ExclamationEqual = \!=
Function = function
Return = return
QuestionMark = \?
ExclamationMark = \!
Colon = :
DubbleAmpersand = &&
DubblePipe = \|\|
While = while
For = for
Semicolon = ;
Println = println
Const = const
Let = let
DubbleColon = ::
Boolean = Bool
FloatingPoint = FloatingPoint
IntegerType = Integer
Int = int
Float = float
Readint = readint
Readfloat = readfloat
Bool = bool
Backslash = \\
Do = do
End = end
Comma = ,
LeftParenthesis = \(
RightParenthesis = \)
MinusSign = -
PlusSign = \+
EqualsSign = =
Asterisk = \*
Slash = \/
True = true
False = false
LowerSign = <
GreaterSign = >
LowerOrEquals = <=
GreaterOrEquals = >=
If = if
Else = else
Elseif = elseif
Identifier = ([a-z]|[A-Z]|_)([a-z]|[A-Z]|[0-9]|_)*
Integer = (\+|-)?[1-9][0-9]*
Real = (\+|-)?[1-9][0-9]*\.[0-9]+




//Declare exclusive states

%%//Identification of tokens

{Comment} {}
{Circumflex} {return new Symbol(LexicalUnit.POWER, yyline, yycolumn, yytext());}
{Percentage} {return new Symbol(LexicalUnit.REMAINDER, yyline, yycolumn, yytext());}
{Tilde} {return new Symbol(LexicalUnit.BITWISE_NOT, yyline, yycolumn, yytext());}
{Ampersand} {return new Symbol(LexicalUnit.BITWISE_AND, yyline, yycolumn, yytext());}
{Pipe} {return new Symbol(LexicalUnit.BITWISE_OR, yyline, yycolumn, yytext());}

{DubbleGreater} {return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_RIGHT, yyline, yycolumn, yytext());}
{DubbleLower} {return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_LEFT, yyline, yycolumn, yytext());}
{DubbleEqual} {return new Symbol(LexicalUnit.EQUALITY, yyline, yycolumn, yytext());}
{ExclamationEqual} {return new Symbol(LexicalUnit.INEQUALITY, yyline, yycolumn, yytext());}
{Function} {return new Symbol(LexicalUnit.FUNCTION, yyline, yycolumn, yytext());}
{Return} {return new Symbol(LexicalUnit.RETURN, yyline, yycolumn, yytext());}

{QuestionMark} {return new Symbol(LexicalUnit.TERNARY_IF, yyline, yycolumn, yytext());}
{ExclamationMark} {return new Symbol(LexicalUnit.NEGATION, yyline, yycolumn, yytext());}

{DubbleAmpersand} {return new Symbol(LexicalUnit.LAZY_AND, yyline, yycolumn, yytext());}
{DubblePipe} {return new Symbol(LexicalUnit.LAZY_OR, yyline, yycolumn, yytext());}
{While} {return new Symbol(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
{For} {return new Symbol(LexicalUnit.FOR, yyline, yycolumn, yytext());}
{Semicolon} {return new Symbol(LexicalUnit.END_OF_INSTRUCTION, yyline, yycolumn, yytext());}
{Println} {return new Symbol(LexicalUnit.PRINTLN, yyline, yycolumn, yytext());}
{Const} {return new Symbol(LexicalUnit.CONST, yyline, yycolumn, yytext());}
{Let} {return new Symbol(LexicalUnit.LET, yyline, yycolumn, yytext());}
{DubbleColon} {return new Symbol(LexicalUnit.TYPE_DEFINITION, yyline, yycolumn, yytext());}
{Boolean} {return new Symbol(LexicalUnit.BOOLEAN_TYPE, yyline, yycolumn, yytext());}
{FloatingPoint} {return new Symbol(LexicalUnit.REAL_TYPE, yyline, yycolumn, yytext());}
{IntegerType} {return new Symbol(LexicalUnit.INTEGER_TYPE, yyline, yycolumn, yytext());}
{Int} {return new Symbol(LexicalUnit.INTEGER_CAST, yyline, yycolumn, yytext());}
{Float} {return new Symbol(LexicalUnit.REAL_CAST, yyline, yycolumn, yytext());}
{Readint} {return new Symbol(LexicalUnit.READ_INTEGER, yyline, yycolumn, yytext());}
{Readfloat} {return new Symbol(LexicalUnit.READ_REAL, yyline, yycolumn, yytext());}
{Bool} {return new Symbol(LexicalUnit.BOOLEAN_CAST, yyline, yycolumn, yytext());}
{Backslash} {return new Symbol(LexicalUnit.INVERSE_DIVIDE, yyline, yycolumn, yytext());}
{Do} {return new Symbol(LexicalUnit.DO, yyline, yycolumn, yytext());}
{End} {return new Symbol(LexicalUnit.END, yyline, yycolumn, yytext());}
{Comma} {return new Symbol(LexicalUnit.COMMA, yyline, yycolumn, yytext());}
{LeftParenthesis} {return new Symbol(LexicalUnit.LEFT_PARENTHESIS, yyline, yycolumn, yytext());}
{RightParenthesis} {return new Symbol(LexicalUnit.RIGHT_PARENTHESIS, yyline, yycolumn, yytext());}
{MinusSign} {return new Symbol(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
{PlusSign} {return new Symbol(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
{EqualsSign} {return new Symbol(LexicalUnit.ASSIGNATION, yyline, yycolumn, yytext());}
{Asterisk} {return new Symbol(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
{Slash} {return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}


{LowerSign} {return new Symbol(LexicalUnit.LESS_THAN, yyline, yycolumn, yytext());}
{GreaterSign} {return new Symbol(LexicalUnit.GREATER_THAN, yyline, yycolumn, yytext());}
{LowerOrEquals} {return new Symbol(LexicalUnit.LESS_OR_EQUALS_THAN, yyline, yycolumn, yytext());}
{GreaterOrEquals} {return new Symbol(LexicalUnit.GREATER_OR_EQUALS_THAN, yyline, yycolumn, yytext());}
{If} {return new Symbol(LexicalUnit.IF, yyline, yycolumn, yytext());}
{Else} {return new Symbol(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
{Elseif} {return new Symbol(LexicalUnit.ELSE_IF, yyline, yycolumn, yytext());}
{Identifier} {
    Symbol identifier = new Symbol(LexicalUnit.IDENTIFIER, yyline, yycolumn, yytext());
    if(!identifiersMap.containsKey(identifier.hashCode())) {
        identifiersMap.put(identifier.hashCode(), identifier);
    }
    return identifier;
}
{Integer} {return new Symbol(LexicalUnit.INTEGER, yyline, yycolumn, yytext());}
{Real} {return new Symbol(LexicalUnit.REAL, yyline, yycolumn, yytext());}



[^] {}
