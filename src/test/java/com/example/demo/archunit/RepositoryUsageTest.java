package com.example.demo.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AnalyzeClasses(packages = "com.example.demo")
class RepositoryUsageTest {

    private static final Set<String> customRepositoryNames;

    static {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.demo"); // 패키지 경로 수정 필요
        customRepositoryNames = importedClasses.stream()
            .filter(clazz -> clazz.isInterface() && clazz.getName().endsWith("Custom"))
            .map(JavaClass::getName)
            .collect(Collectors.toSet());
    }

    @ArchTest
    @DisplayName("서비스 계층에서 Custom Repository가 아닌 정식 Repository를 사용해야 한다 (필드, 생성자, 메서드 검사)")
    void service_layer_should_not_use_custom_repositories(JavaClasses importedClasses) {
        importedClasses.stream()
            .filter(clazz -> clazz.isAnnotatedWith(Service.class))  // @Service가 있는 클래스만 필터링
            .forEach(serviceClass -> {
                // 필드 검사
                serviceClass.getFields().forEach(field -> checkViolation(field, field.getRawType().getName()));
                // 메서드 검사
                serviceClass.getMethods().forEach(method ->
                    method.getRawParameterTypes().forEach(paramType -> checkViolation(method, paramType.getName()))
                );
                // 생성자 검사
                serviceClass.getConstructors().forEach(constructor ->
                    constructor.getRawParameterTypes().forEach(paramType -> checkViolation(constructor, paramType.getName()))
                );
            });
    }

    private void checkViolation(JavaMember member, String typeName) {
        if (customRepositoryNames.contains(typeName)) {
            throw new AssertionError(String.format(
                "❌ Violation: %s in class %s should not use Custom Repository %s",
                member.getName(), member.getOwner().getName(), typeName
            ));
        }
    }
}
