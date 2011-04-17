package example1.generator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** DTO for each object in the yaml file. */
public class ObjectSpec {

  public final String name;
  public final List<AttributeSpec> attributes = new ArrayList<AttributeSpec>();

  /**
   * @param e
   *          a {@code Map.Entry} from our object name -> a list of attribute specs
   */
  public ObjectSpec(final Map.Entry<String, List<String>> e) {
    name = e.getKey();
    for (final String attribute : e.getValue()) {
      attributes.add(new AttributeSpec(attribute));
    }
  }

  public String builderName() {
    return name + "Builder";
  }

}