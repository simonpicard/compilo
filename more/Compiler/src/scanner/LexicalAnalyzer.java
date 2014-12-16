/* The following code was generated by JFlex 1.4.3 on 16/12/14 17:16 */

package scanner;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 16/12/14 17:16 from the specification file
 * <tt>LexicalAnalyzer.flex</tt>
 */
public class LexicalAnalyzer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\63\1\2\2\0\1\1\22\0\1\63\1\15\1\0\1\3"+
    "\1\11\1\5\1\7\1\0\1\52\1\53\1\55\1\54\1\51\1\27"+
    "\1\62\1\56\1\60\11\61\1\31\1\35\1\13\1\14\1\12\1\30"+
    "\1\0\1\57\1\40\3\57\1\41\2\57\1\45\6\57\1\44\12\57"+
    "\1\0\1\50\1\0\1\4\1\57\1\0\1\42\1\47\1\21\1\46"+
    "\1\26\1\16\1\43\1\33\1\23\2\57\1\34\1\57\1\20\1\24"+
    "\1\36\1\57\1\25\1\37\1\22\1\17\1\57\1\32\3\57\1\0"+
    "\1\10\1\0\1\6\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\1\1\1\3\1\4\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\7\15\1\16"+
    "\1\17\1\20\10\15\1\21\1\22\1\23\1\24\1\25"+
    "\1\26\1\27\2\30\1\31\1\0\1\2\1\32\1\33"+
    "\1\34\1\35\1\36\1\37\1\40\1\41\6\15\1\42"+
    "\4\15\1\43\1\44\6\15\1\45\1\15\1\1\1\0"+
    "\1\15\1\46\4\15\1\47\2\15\1\50\2\15\1\51"+
    "\5\15\1\0\1\52\3\15\1\53\2\15\1\54\2\15"+
    "\1\55\2\15\1\56\1\1\1\15\1\57\1\60\4\15"+
    "\1\61\4\15\1\62\2\15\1\63\5\15\1\64\1\65"+
    "\1\15\1\66\1\67\2\15\1\70\4\15\1\71";

  private static int [] zzUnpackAction() {
    int [] result = new int[144];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\64\0\150\0\64\0\234\0\64\0\64\0\64"+
    "\0\320\0\u0104\0\64\0\u0138\0\u016c\0\u01a0\0\u01d4\0\u0208"+
    "\0\u023c\0\u0270\0\u02a4\0\u02d8\0\u030c\0\u0340\0\u0374\0\64"+
    "\0\u03a8\0\u03dc\0\u0410\0\u0444\0\u0478\0\u04ac\0\u04e0\0\u0514"+
    "\0\u0548\0\64\0\64\0\64\0\64\0\64\0\64\0\64"+
    "\0\u057c\0\u05b0\0\64\0\234\0\234\0\64\0\64\0\64"+
    "\0\64\0\64\0\64\0\64\0\64\0\u05e4\0\u0618\0\u064c"+
    "\0\u0680\0\u06b4\0\u06e8\0\u023c\0\u071c\0\u0750\0\u0784\0\u07b8"+
    "\0\64\0\64\0\u07ec\0\u0820\0\u0854\0\u0888\0\u08bc\0\u08f0"+
    "\0\u023c\0\u0924\0\u0958\0\u098c\0\u09c0\0\u023c\0\u09f4\0\u0a28"+
    "\0\u0a5c\0\u0a90\0\u023c\0\u0ac4\0\u0af8\0\u023c\0\u0b2c\0\u0b60"+
    "\0\u023c\0\u0b94\0\u0bc8\0\u0bfc\0\u0c30\0\u0c64\0\u0c98\0\u098c"+
    "\0\u0ccc\0\u0d00\0\u0d34\0\u023c\0\u0d68\0\u0d9c\0\u0dd0\0\u0e04"+
    "\0\u0e38\0\u023c\0\u0e6c\0\u0ea0\0\u023c\0\u0c98\0\u0ed4\0\u023c"+
    "\0\u023c\0\u0f08\0\u0f3c\0\u0f70\0\u0fa4\0\u023c\0\u0fd8\0\u100c"+
    "\0\u1040\0\u1074\0\u023c\0\u10a8\0\u10dc\0\u023c\0\u1110\0\u1144"+
    "\0\u1178\0\u11ac\0\u11e0\0\u023c\0\u023c\0\u1214\0\u023c\0\u023c"+
    "\0\u1248\0\u127c\0\u023c\0\u12b0\0\u12e4\0\u1318\0\u134c\0\u023c";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[144];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\17\1\20\2\21"+
    "\1\22\1\23\1\24\1\21\1\25\1\26\1\27\1\30"+
    "\1\31\1\32\1\21\1\33\1\4\1\34\1\21\1\35"+
    "\1\36\3\21\1\37\1\40\1\41\1\42\1\43\1\44"+
    "\1\45\1\46\1\47\1\50\1\21\1\51\1\52\1\2"+
    "\1\53\66\0\1\4\61\0\1\54\1\55\1\4\61\54"+
    "\7\0\1\56\64\0\1\57\65\0\1\60\1\0\1\61"+
    "\62\0\1\62\1\63\63\0\1\64\63\0\1\65\65\0"+
    "\1\21\1\66\4\21\1\67\2\21\3\0\2\21\1\70"+
    "\1\0\4\21\1\71\5\21\7\0\3\21\20\0\11\21"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\6\21"+
    "\1\72\2\21\3\0\3\21\1\0\12\21\7\0\3\21"+
    "\20\0\7\21\1\73\1\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\1\74\1\21\1\75\6\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\10\21\1\76"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\2\21"+
    "\1\77\6\21\3\0\2\21\1\100\1\0\12\21\7\0"+
    "\3\21\14\0\1\101\102\0\1\102\50\0\11\21\3\0"+
    "\1\21\1\103\1\21\1\0\12\21\7\0\3\21\20\0"+
    "\10\21\1\104\3\0\3\21\1\0\12\21\7\0\3\21"+
    "\20\0\7\21\1\105\1\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\6\21\1\106\2\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\11\21\3\0\2\21"+
    "\1\107\1\0\12\21\7\0\3\21\20\0\2\21\1\110"+
    "\6\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\6\21\1\111\2\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\6\21\1\112\2\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\62\0\2\113\1\114\61\0\2\52"+
    "\1\114\17\0\2\21\1\115\6\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\7\21\1\116\1\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\6\21\1\117"+
    "\2\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\11\21\3\0\2\21\1\120\1\0\12\21\7\0\3\21"+
    "\20\0\2\21\1\121\6\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\1\21\1\122\7\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\4\21\1\123\4\21"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\4\21"+
    "\1\124\4\21\3\0\3\21\1\0\4\21\1\125\5\21"+
    "\7\0\3\21\20\0\11\21\3\0\3\21\1\0\10\21"+
    "\1\126\1\21\7\0\3\21\20\0\11\21\3\0\3\21"+
    "\1\0\1\21\1\127\10\21\7\0\3\21\20\0\5\21"+
    "\1\130\3\21\3\0\3\21\1\0\12\21\7\0\3\21"+
    "\20\0\4\21\1\131\4\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\5\21\1\132\3\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\6\21\1\133\2\21"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\6\21"+
    "\1\134\2\21\3\0\3\21\1\0\12\21\7\0\3\21"+
    "\20\0\4\21\1\135\4\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\6\21\1\136\2\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\62\0\2\113\1\137\61\0"+
    "\2\140\20\0\3\21\1\141\5\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\11\21\3\0\3\21\1\0"+
    "\4\21\1\142\5\21\7\0\3\21\20\0\11\21\3\0"+
    "\3\21\1\0\1\21\1\122\10\21\7\0\3\21\20\0"+
    "\11\21\3\0\3\21\1\0\1\21\1\143\10\21\7\0"+
    "\3\21\20\0\10\21\1\144\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\1\21\1\145\7\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\11\21\3\0\3\21"+
    "\1\0\10\21\1\146\1\21\7\0\3\21\20\0\10\21"+
    "\1\147\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\11\21\3\0\2\21\1\150\1\0\12\21\7\0\3\21"+
    "\20\0\2\21\1\151\6\21\3\0\3\21\1\0\12\21"+
    "\7\0\3\21\20\0\11\21\3\0\2\21\1\152\1\0"+
    "\12\21\7\0\3\21\20\0\11\21\3\0\3\21\1\0"+
    "\4\21\1\153\5\21\7\0\3\21\20\0\10\21\1\154"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\11\21"+
    "\3\0\2\21\1\155\1\0\12\21\7\0\3\21\62\0"+
    "\2\156\20\0\4\21\1\157\4\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\4\21\1\160\4\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\4\21\1\161"+
    "\4\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\7\21\1\162\1\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\1\163\4\21\1\164\3\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\5\21\1\165\3\21"+
    "\3\0\3\21\1\0\12\21\7\0\3\21\20\0\10\21"+
    "\1\166\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\4\21\1\167\4\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\4\21\1\170\4\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\11\21\3\0\3\21\1\0"+
    "\5\21\1\171\4\21\7\0\3\21\20\0\5\21\1\172"+
    "\3\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\2\21\1\173\6\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\11\21\3\0\2\21\1\174\1\0\12\21"+
    "\7\0\3\21\20\0\2\21\1\175\6\21\3\0\3\21"+
    "\1\0\12\21\7\0\3\21\20\0\1\176\10\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\11\21\3\0"+
    "\2\21\1\177\1\0\12\21\7\0\3\21\20\0\5\21"+
    "\1\200\3\21\3\0\3\21\1\0\12\21\7\0\3\21"+
    "\20\0\10\21\1\201\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\6\21\1\202\2\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\6\21\1\203\2\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\4\21\1\204"+
    "\4\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\2\21\1\205\6\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\2\21\1\206\6\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\7\21\1\207\1\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\2\21\1\210"+
    "\6\21\3\0\3\21\1\0\12\21\7\0\3\21\20\0"+
    "\11\21\3\0\3\21\1\0\4\21\1\211\5\21\7\0"+
    "\3\21\20\0\11\21\3\0\3\21\1\0\5\21\1\212"+
    "\4\21\7\0\3\21\20\0\4\21\1\213\4\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\11\21\3\0"+
    "\3\21\1\0\6\21\1\214\3\21\7\0\3\21\20\0"+
    "\6\21\1\215\2\21\3\0\3\21\1\0\12\21\7\0"+
    "\3\21\20\0\5\21\1\216\3\21\3\0\3\21\1\0"+
    "\12\21\7\0\3\21\20\0\2\21\1\217\6\21\3\0"+
    "\3\21\1\0\12\21\7\0\3\21\20\0\4\21\1\220"+
    "\4\21\3\0\3\21\1\0\12\21\7\0\3\21\2\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4992];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\1\1\3\11\2\1\1\11"+
    "\14\1\1\11\11\1\7\11\2\1\1\11\1\0\1\1"+
    "\10\11\13\1\2\11\11\1\1\0\22\1\1\0\61\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[144];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
    private static HashMap<Object, Integer> identifiersMap = new HashMap<Object, Integer>();
    public static HashMap<Object, Integer> getIdentifiersMap() {
        return identifiersMap;
    }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public LexicalAnalyzer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public LexicalAnalyzer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 148) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Symbol nextToken() throws java.io.IOException, PatternSyntaxException, SyntaxErrorException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 10: 
          { return new Symbol(LexicalUnit.LESS_THAN, yyline, yycolumn, yytext());
          }
        case 58: break;
        case 47: 
          { return new Symbol(LexicalUnit.REAL_CAST, yyline, yycolumn, yytext());
          }
        case 59: break;
        case 52: 
          { return new Symbol(LexicalUnit.READ_INTEGER, yyline, yycolumn, yytext());
          }
        case 60: break;
        case 37: 
          { return new Symbol(LexicalUnit.DO, yyline, yycolumn, yytext());
          }
        case 61: break;
        case 24: 
          { return new Symbol(LexicalUnit.INTEGER, yyline, yycolumn, yytext());
          }
        case 62: break;
        case 2: 
          { return new Symbol(LexicalUnit.END_OF_INSTRUCTION, yyline, yycolumn, yytext());
          }
        case 63: break;
        case 48: 
          { return new Symbol(LexicalUnit.CONST, yyline, yycolumn, yytext());
          }
        case 64: break;
        case 32: 
          { return new Symbol(LexicalUnit.EQUALITY, yyline, yycolumn, yytext());
          }
        case 65: break;
        case 34: 
          { return new Symbol(LexicalUnit.IF, yyline, yycolumn, yytext());
          }
        case 66: break;
        case 12: 
          { return new Symbol(LexicalUnit.NEGATION, yyline, yycolumn, yytext());
          }
        case 67: break;
        case 50: 
          { return new Symbol(LexicalUnit.RETURN, yyline, yycolumn, yytext());
          }
        case 68: break;
        case 44: 
          { return new Symbol(LexicalUnit.ELSE, yyline, yycolumn, yytext());
          }
        case 69: break;
        case 38: 
          { return new Symbol(LexicalUnit.FOR, yyline, yycolumn, yytext());
          }
        case 70: break;
        case 28: 
          { return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_RIGHT, yyline, yycolumn, yytext());
          }
        case 71: break;
        case 40: 
          { return new Symbol(LexicalUnit.END, yyline, yycolumn, yytext());
          }
        case 72: break;
        case 45: 
          { return new Symbol(LexicalUnit.BOOLEAN_TYPE, yyline, yycolumn, yytext());
          }
        case 73: break;
        case 36: 
          { return new Symbol(LexicalUnit.TYPE_DEFINITION, yyline, yycolumn, yytext());
          }
        case 74: break;
        case 21: 
          { return new Symbol(LexicalUnit.PLUS, yyline, yycolumn, yytext());
          }
        case 75: break;
        case 57: 
          { return new Symbol(LexicalUnit.REAL_TYPE, yyline, yycolumn, yytext());
          }
        case 76: break;
        case 14: 
          { return new Symbol(LexicalUnit.MINUS, yyline, yycolumn, yytext());
          }
        case 77: break;
        case 5: 
          { return new Symbol(LexicalUnit.BITWISE_NOT, yyline, yycolumn, yytext());
          }
        case 78: break;
        case 23: 
          { return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());
          }
        case 79: break;
        case 31: 
          { return new Symbol(LexicalUnit.LESS_OR_EQUALS_THAN, yyline, yycolumn, yytext());
          }
        case 80: break;
        case 17: 
          { return new Symbol(LexicalUnit.INVERSE_DIVIDE, yyline, yycolumn, yytext());
          }
        case 81: break;
        case 49: 
          { return new Symbol(LexicalUnit.WHILE, yyline, yycolumn, yytext());
          }
        case 82: break;
        case 1: 
          { throw new SyntaxErrorException(yytext());
          }
        case 83: break;
        case 19: 
          { return new Symbol(LexicalUnit.LEFT_PARENTHESIS, yyline, yycolumn, yytext());
          }
        case 84: break;
        case 15: 
          { return new Symbol(LexicalUnit.TERNARY_IF, yyline, yycolumn, yytext());
          }
        case 85: break;
        case 26: 
          { return new Symbol(LexicalUnit.LAZY_AND, yyline, yycolumn, yytext());
          }
        case 86: break;
        case 39: 
          { return new Symbol(LexicalUnit.INTEGER_CAST, yyline, yycolumn, yytext());
          }
        case 87: break;
        case 55: 
          { return new Symbol(LexicalUnit.FUNCTION, yyline, yycolumn, yytext());
          }
        case 88: break;
        case 22: 
          { return new Symbol(LexicalUnit.TIMES, yyline, yycolumn, yytext());
          }
        case 89: break;
        case 56: 
          { return new Symbol(LexicalUnit.READ_REAL, yyline, yycolumn, yytext());
          }
        case 90: break;
        case 9: 
          { return new Symbol(LexicalUnit.GREATER_THAN, yyline, yycolumn, yytext());
          }
        case 91: break;
        case 6: 
          { return new Symbol(LexicalUnit.BITWISE_AND, yyline, yycolumn, yytext());
          }
        case 92: break;
        case 29: 
          { return new Symbol(LexicalUnit.GREATER_OR_EQUALS_THAN, yyline, yycolumn, yytext());
          }
        case 93: break;
        case 11: 
          { return new Symbol(LexicalUnit.ASSIGNATION, yyline, yycolumn, yytext());
          }
        case 94: break;
        case 53: 
          { return new Symbol(LexicalUnit.PRINTLN, yyline, yycolumn, yytext());
          }
        case 95: break;
        case 4: 
          { return new Symbol(LexicalUnit.REMAINDER, yyline, yycolumn, yytext());
          }
        case 96: break;
        case 20: 
          { return new Symbol(LexicalUnit.RIGHT_PARENTHESIS, yyline, yycolumn, yytext());
          }
        case 97: break;
        case 16: 
          { return new Symbol(LexicalUnit.TERNARY_ELSE, yyline, yycolumn, yytext());
          }
        case 98: break;
        case 54: 
          { return new Symbol(LexicalUnit.INTEGER_TYPE, yyline, yycolumn, yytext());
          }
        case 99: break;
        case 42: 
          { return new Symbol(LexicalUnit.REAL, yyline, yycolumn, yytext());
          }
        case 100: break;
        case 7: 
          { return new Symbol(LexicalUnit.BITWISE_OR, yyline, yycolumn, yytext());
          }
        case 101: break;
        case 33: 
          { return new Symbol(LexicalUnit.INEQUALITY, yyline, yycolumn, yytext());
          }
        case 102: break;
        case 30: 
          { return new Symbol(LexicalUnit.ARITHMETIC_SHIFT_LEFT, yyline, yycolumn, yytext());
          }
        case 103: break;
        case 3: 
          { return new Symbol(LexicalUnit.POWER, yyline, yycolumn, yytext());
          }
        case 104: break;
        case 51: 
          { return new Symbol(LexicalUnit.ELSE_IF, yyline, yycolumn, yytext());
          }
        case 105: break;
        case 18: 
          { return new Symbol(LexicalUnit.COMMA, yyline, yycolumn, yytext());
          }
        case 106: break;
        case 35: 
          { return new Symbol(LexicalUnit.MAP_TO, yyline, yycolumn, yytext());
          }
        case 107: break;
        case 41: 
          { return new Symbol(LexicalUnit.LET, yyline, yycolumn, yytext());
          }
        case 108: break;
        case 13: 
          { Symbol identifier = new Symbol(LexicalUnit.IDENTIFIER, yyline, yycolumn, yytext());
    if(!identifiersMap.containsKey(identifier.getValue())) {
        identifiersMap.put(identifier.getValue(), identifier.getLine());
    }
    return identifier;
          }
        case 109: break;
        case 43: 
          { return new Symbol(LexicalUnit.BOOLEAN, yyline, yycolumn, yytext());
          }
        case 110: break;
        case 46: 
          { return new Symbol(LexicalUnit.BOOLEAN_CAST, yyline, yycolumn, yytext());
          }
        case 111: break;
        case 27: 
          { return new Symbol(LexicalUnit.LAZY_OR, yyline, yycolumn, yytext());
          }
        case 112: break;
        case 25: 
          { 
          }
        case 113: break;
        case 8: 
          { return new Symbol(LexicalUnit.BITWISE_XOR, yyline, yycolumn, yytext());
          }
        case 114: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
              { 	return new Symbol(LexicalUnit.END_OF_STREAM,yyline, yycolumn);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
