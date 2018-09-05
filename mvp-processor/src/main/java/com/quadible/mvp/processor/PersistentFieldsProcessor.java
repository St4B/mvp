package com.quadible.mvp.processor;

import com.quadible.mvp.annotation.DataContainer;
import com.quadible.mvp.annotation.Persistable;
import com.quadible.mvp.annotation.Persistent;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Generates classes for storing the {@link Persistent} fields of a {@link Persistable} object.
 * The generated classes are named as "{@link Persistable} object's name +
 * {@link DataContainer#SUFFIX}". We generate classes in order to make easier the
 * serialization/de-serialization as we have strict types for the fields. Also, by having the same
 * field names in the generated classes we can map the values at runtime.
 */
public class PersistentFieldsProcessor extends AbstractProcessor {

    private Filer mFiler;

    private Elements mElementUtils;

    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Persistable.class)) {

            if (element.getKind() != ElementKind.CLASS) {
                mMessager.printMessage(Diagnostic.Kind.ERROR,
                        "@Persistable can only be applied to class. "
                                + element.getSimpleName().toString() + " is not a class");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            TypeSpec.Builder persistentValuesClass = generatePersistentValuesClass(typeElement);

            //Search for persistent fields inside a persistable class
            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();

            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement.getAnnotation(Persistent.class) == null) {
                    continue;
                }

                if (enclosedElement.getKind() != ElementKind.FIELD) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR,
                            "@Persistent can only be applied to fields. "
                                    + element.getSimpleName().toString() + " is not a field");
                    return true;
                }

                VariableElement variableElement = (VariableElement) enclosedElement;

                //Add the persistent field in data container
                FieldSpec field = generatePersistentField(variableElement);
                persistentValuesClass.addField(field);
            }

            try {
                JavaFile.builder(mElementUtils.getPackageOf(element).toString(),
                        persistentValuesClass.build())
                        .build()
                        .writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private TypeSpec.Builder generatePersistentValuesClass(TypeElement element) {
        return TypeSpec.classBuilder(element.getSimpleName().toString() + DataContainer.SUFFIX)
                .addSuperinterface(DataContainer.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    private FieldSpec generatePersistentField(VariableElement element) {
        TypeName typeName = TypeName.get(element.asType());

        return FieldSpec.builder(typeName,
                element.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Persistable.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
