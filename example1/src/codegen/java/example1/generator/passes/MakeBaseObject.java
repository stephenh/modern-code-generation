package example1.generator.passes;

import static joist.sourcegen.Argument.arg;

import java.util.ArrayList;
import java.util.List;

import example1.generator.Gen;
import example1.generator.model.AttributeSpec;
import example1.generator.model.ObjectSpec;

import joist.sourcegen.GClass;
import joist.sourcegen.GDirectory;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;

/** Makes a basic DTO with getters/setters. */
public class MakeBaseObject {

  public static void make(final GDirectory out, final ObjectSpec object) {
    final GClass g = out.getClass(Gen.objectsPackage + "." + object.name);

    for (final AttributeSpec attribute : object.attributes) {
      addField(g, attribute);
      addGetter(g, attribute);
      addSetter(g, attribute);
    }
  }

  private static void addField(final GClass g, final AttributeSpec attribute) {
    final GField f = g.getField(attribute.name).type(attribute.type);
    if (attribute.isList()) {
      f.initialValue("new ArrayList<{}>()", attribute.getListType());
      g.addImports(List.class);
    }
  }

  private static void addGetter(final GClass g, final AttributeSpec attribute) {
    final GMethod getter = g.getMethod(attribute.getterName()).returnType(attribute.type);
    getter.body.line("return this.{};", attribute.name);
  }

  private static void addSetter(final GClass g, final AttributeSpec attribute) {
    final GMethod setter = g.getMethod(attribute.setterName(), arg(attribute.type, attribute.name));
    if (attribute.isList()) {
      setter.body.line("this.{} = new ArrayList<{}>({});", attribute.name, attribute.getListType(), attribute.name);
      g.addImports(ArrayList.class);
    } else {
      setter.body.line("this.{} = {};", attribute.name, attribute.name);
    }
  }

}
