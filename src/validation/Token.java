package validation;

/**
 * Created by Hans on 29/10/2015.
 */
public class Token {

  private boolean activated = false;
  public int id = 0;
  private int count;

  public Token(int id) {
    this.id = id;
    this.activated = true;
    this.count = 0;
  }

  public boolean consumeToken() {
    if (count >= 1) {
      count--;
      return activated;
    } else {
      activated = false;
      return false;
    }
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

  public void incrementCount() {
    count++;
    if(count >= 1) {
      activated = true;
    }
  }

  @Override
  public int hashCode() {
    return id;
  }
}
