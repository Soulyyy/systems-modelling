package validation;

/**
 * Created by Hans on 29/10/2015.
 */
public class Token {

  boolean activated = false;
  public int id = 0;

  public Token(boolean activated) {
    this.activated = activated;
  }

  public Token() {
    this.activated = true;
  }

  public Token(int id) {
    this.id = id;
    this.activated = true;
  }

  public boolean consumeToken() {
    boolean resp = activated;
    activated = false;
    return resp;
  }

  public boolean isActivated() {
    return activated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Token token = (Token) o;

    if (id != token.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
