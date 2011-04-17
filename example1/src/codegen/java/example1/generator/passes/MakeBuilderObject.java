package example1.generator.passes;

import static joist.sourcegen.Argument.arg;

import java.util.ArrayList;
import java.util.List;

import example1.generator.Gen;
import example1.generator.model.AttributeSpec;
import example1.generator.model.ObjectSpec;

import joist.sourcegen.GClass;
import joist.sourcegen.GDirectory;
import joist.sourcegen.GMethod;

/**
 * Makes builders to ensure objects are fully setup before being initialized.
 * 
 * E.g.:
 * 
 * new EmployerBuilder().name("blah").build();
 * 
 * {@code build} will fail if not all of Partner's attributes are set.
 */
public class MakeBuilderObject {

  public static void make(final GDirectory out, final ObjectSpec object) {
    final GClass g = out.getClass(Gen.objectsPackage + "." + object.builderName());
    final GMethod cstr = g.getConstructor();

    addFieldToTrackUnAssignedAttributes(g);
    addFieldForObjectBeingBuilt(g, object);
    addBuildMethod(g, object);

    for (final AttributeSpec attribute : object.attributes) {
      addAttributeNameToUnAssignedAttributes(cstr, attribute);
      addFluentSetter(g, object, attribute);
    }
  }

  private static void addFieldToTrackUnAssignedAttributes(final GClass g) {
    g.getField("unassigned").type("List<String>").initialValue("new ArrayList<String>()");
    g.addImports(List.class, ArrayList.class);
  }

  private static void addFieldForObjectBeingBuilt(final GClass g, final ObjectSpec object) {
    g.getField("object").type(object.name).initialValue("new {}()", object.name);
  }

  private static void addBuildMethod(final GClass g, final ObjectSpec object) {
    final GMethod m = g.getMethod("build").returnType(object.name);
    m.body.line("if (!unassigned.isEmpty()) {");
    m.body.line("    throw new IllegalStateException(\"Remaining fields unassigned: \" + unassigned);");
    m.body.line("}");
    m.body.line("return object;");
  }

  private static void addAttributeNameToUnAssignedAttributes(final GMethod cstr, final AttributeSpec attribute) {
    cstr.body.line("unassigned.add(\"{}\");", attribute.name);
  }

  private static void addFluentSetter(final GClass g, final ObjectSpec object, final AttributeSpec attribute) {
    final GMethod setter =
      g.getMethod(attribute.name, arg(attribute.type(), attribute.name)).returnType(object.builderName());
    setter.body.line("object.{}({});", attribute.setterName(), attribute.name);
    setter.body.line("unassigned.remove(\"{}\");", attribute.name);
    setter.body.line("return this;");
  }

}
