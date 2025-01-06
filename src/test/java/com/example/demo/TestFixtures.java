package com.example.demo;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import java.util.List;

public class TestFixtures {

    private static final FixtureMonkey fixtureMonkey;

    static {
        fixtureMonkey = FixtureMonkey.builder()
            .plugin(new JakartaValidationPlugin())
            .enableLoggingFail(false)
            .objectIntrospector(new FailoverIntrospector(
                List.of(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            ))
            .build();
    }

    public static FixtureMonkey get() {
        return fixtureMonkey;
    }
}
