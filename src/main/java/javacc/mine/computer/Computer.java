/* Generated By:JavaCC: Do not edit this line. Computer.java */
//定义�?package javacc.mine.computer;

import java.io.PrintStream;
//定义解析器名称Computer该类实现了四则运�?
public class Computer implements ComputerConstants {
        // 定义参数
        double previousValue = 0.0;

        public static void main(String[] args) throws ParseException, TokenMgrError, NumberFormatException {
                Computer parser = new Computer(System.in);
                parser.start(System.out);
        }

/** * 以下均是解析器的定义,解析器就是对各个Token的解�?.对应的事核心类的方法
 *启动,这里由上层Computer类调�?
 */
  final public void start(PrintStream printStream) throws ParseException, NumberFormatException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MINUS:
      case NUMBER:
      case OPEN_PAR:
      case PREVIOUS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      previousValue = expression();
      printStream.println( previousValue ) ;
    }
  }

  final public double expression() throws ParseException, NumberFormatException {
  //第一个括号内�?,定义该方法的临时变量
  double i ;
  double value ;
    value = term();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
        i = term();
      value += i ;
        break;
      case MINUS:
        jj_consume_token(MINUS);
        i = term();
      value -= i ;
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return value ;}
    throw new Error("Missing return statement in function");
  }

  final public double term() throws ParseException, NumberFormatException {
  double i ;
  double value ;
    value = primary();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
      case DIVIDE:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
        jj_consume_token(TIMES);
        i = primary();
      value *= i ;
        break;
      case DIVIDE:
        jj_consume_token(DIVIDE);
        i = primary();
      value /= i ;
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return value ;}
    throw new Error("Missing return statement in function");
  }

  final public double primary() throws ParseException, NumberFormatException {
  Token t ;
  double d ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      t = jj_consume_token(NUMBER);
    {if (true) return Double.parseDouble( t.image ) ;}
      break;
    case PREVIOUS:
      jj_consume_token(PREVIOUS);
    {if (true) return previousValue ;}
      break;
    case OPEN_PAR:
      jj_consume_token(OPEN_PAR);
      d = expression();
      jj_consume_token(CLOSE_PAR);
    {if (true) return d ;}
      break;
    case MINUS:
      jj_consume_token(MINUS);
      d = primary();
    {if (true) return -d ;}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ComputerTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[6];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xa90,0x18,0x18,0x60,0x60,0xa90,};
   }

  /** Constructor with InputStream. */
  public Computer(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Computer(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ComputerTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Computer(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ComputerTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Computer(ComputerTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ComputerTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[12];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 6; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 12; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
