import java.util.regex.PatternSyntaxException;

%%// Options of the scanner

%class LexicalAnalyzer	//Name
%unicode						//Use unicode
%line							//Use line counter (yyline variable)
%column						//Use character counter by line (yycolumn variable)
%function nextToken
%type Symbol
%yylexthrow PatternSyntaxException


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



//Declare exclusive states

%%//Identification of tokens

{Comment} {System.out.println("Comment");}
{Circumflex} {return new Symbol(LexicalUnit.POWER, yyline, yycolumn, yytext());}
{Percentage} {return new Symbol(LexicalUnit.REMAINDER, yyline, yycolumn, yytext());}
{Tilde} {return new Symbol(LexicalUnit.BITWISE_NOT, yyline, yycolumn, yytext());}
{Ampersand} {return new Symbol(LexicalUnit.BITWISE_AND, yyline, yycolumn, yytext());}
{Pipe} {return new Symbol(LexicalUnit.BITWISE_OR, yyline, yycolumn, yytext());}

{DubbleGreater} {return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_RIGHT, yyline, yycolumn, yytext());}
{DubbleLower} {return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_LEFT, yyline, yycolumn, yytext());}
{DubbleEqual} {return new Symbol(LexicalUnit.EQUALITY, yyline, yycolumn, yytext());}
{ExclamationEqual} {return new Symbol(LexicalUnit.INEQUALITY, yyline, yycolumn, yytext());}
"function" {return new Symbol(LexicalUnit.FUNCTION, yyline, yycolumn, yytext());}
"return" {return new Symbol(LexicalUnit.RETURN, yyline, yycolumn, yytext());}



[^] {System.out.println("Illegal char");}
