package example1.generator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import joist.sourcegen.GDirectory;

import org.yaml.snakeyaml.Yaml;

import example1.generator.model.ObjectSpec;
import example1.generator.passes.MakeBaseObject;
import example1.generator.passes.MakeBuilderObject;

/** Generates the DTOs from the {@code objects.yaml} config file. */
public class Gen {

  public static final String yamlConfigFile = "./src/main/specs/objects.yaml";
  public static final String objectsPackage = "example1.objects";

  public static void main(final String[] args) throws Exception {
    final GDirectory out = new GDirectory("./target/gen-java-src");

    for (final ObjectSpec object : parseConfig()) {
      System.out.println("Creating " + object.name);
      MakeBaseObject.make(out, object);
      MakeBuilderObject.make(out, object);
    }

    out.output();
    out.pruneIfNotTouched();
  }
  
  private static List<ObjectSpec> parseConfig() throws Exception {
    final List<ObjectSpec> objects = new ArrayList<ObjectSpec>();
    for (final Entry<String, List<String>> e : readConfig().entrySet()) {
      objects.add(new ObjectSpec(e));
    }
    return objects;
  }

  /** @return map of objectName -> [attributeSpecs] */
  @SuppressWarnings("unchecked")
  private static Map<String, List<String>> readConfig() throws Exception {
    final FileInputStream in = new FileInputStream(yamlConfigFile);
    try {
      return (Map<String, List<String>>) new Yaml().load(in);
    } finally {
      in.close();
    }
  }

}
