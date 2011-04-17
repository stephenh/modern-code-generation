package example1.generator.model;

/** DTO for each attribute of the objects in the yaml file. */
public class AttributeSpec {

  public final String name;
  public final String type;

  public AttributeSpec(final String attribute) {
    final int firstSpace = attribute.indexOf(' ');
    name = attribute.substring(0, firstSpace);
    type = attribute.substring(firstSpace + 1);
  }

  public String getterName() {
      return "get" + capitalize(name);
  }

  public String setterName() {
      return "set" + capitalize(name);
  }

  /** @return true if this attribute is of type {@code List<T>} */
  public boolean isList() {
    return type.startsWith("List<");
  }

  /** @return the {@code T} part of {@code List<T>} */
  public String getListType() {
    return type.substring(5, type.length() - 1);
  }

  /** @return our type, fully qualified if not a java.lang type */
  public String type() {
    return type;
  }
  
  private static String capitalize(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

}