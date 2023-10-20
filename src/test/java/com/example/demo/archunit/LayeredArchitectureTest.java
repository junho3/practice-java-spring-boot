package com.example.demo.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.example.demo")
public class LayeredArchitectureTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()
        .layer("Controllers").definedBy("com.example.demo.web.v1..")
        .layer("Services").definedBy("com.example.demo.core..service..")
        .layer("Persistence").definedBy("com.example.demo.infrastructure.persistence..")
        .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
        .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");
}
