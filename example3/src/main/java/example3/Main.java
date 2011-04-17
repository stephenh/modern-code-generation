package example3;

import lombok.val;

public class Main {

  public static void main(String[] args) {
    val er1 = new Employer(1, "foo");
    val er2 = new Employer(1, "foo");
    System.out.println(er1.equals(er2));
  }

}
