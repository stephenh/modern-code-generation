package example2.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import joist.sourcegen.GClass;

import org.exigencecorp.aptutil.Util;

import example2.GenAnnotation;

@SupportedAnnotationTypes({ "example2.GenAnnotation" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(GenAnnotation.class)) {
			if (element.getKind() == ElementKind.CLASS) {
				generate((TypeElement) element);
			}
		}
		return true;
	}

	private void generate(TypeElement userClass) {
		String newClassName = userClass.toString() + "New";

		GClass g = new GClass(newClassName);
		g.addAnnotation("@SuppressWarnings(\"all\")");

    for (Element enclosed : userClass.getEnclosedElements()) {
      if (enclosed.getKind() == ElementKind.FIELD) {
        VariableElement ve = (VariableElement) enclosed;
        g.getField(ve.getSimpleName().toString()).type("String");
      }
    }

		Util.saveCode(processingEnv, g, userClass);
	}

}
