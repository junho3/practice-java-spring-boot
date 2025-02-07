package com.example.demo.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.DisplayName;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packages = "com.example.demo")
public class TransactionalAnnotationTest {

    @ArchTest
    @DisplayName("@Transactional(readOnly = true)를 선언한 곳이 있는지 검사")
    void transactional_annotation_with_readOnly_true_should_not_use(JavaClasses importedClasses) {
        methods().that().areAnnotatedWith(Transactional.class)
            .should(new ArchCondition<>("not have readOnly = true") {
                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                    method.getAnnotations().stream()
                        .filter(annotation -> annotation.getRawType().isEquivalentTo(Transactional.class))
                        .forEach(annotation -> {
                            Map<String, Object> properties = annotation.getProperties();
                            if (properties.containsKey("readOnly") && Boolean.TRUE.equals(properties.get("readOnly"))) {
                                events.add(SimpleConditionEvent.violated(method,
                                    method.getFullName() + " should use @ReadTransactional instead of @Transactional(readOnly = true)"));
                            }
                        });
                }
            }).check(importedClasses);
    }
}
