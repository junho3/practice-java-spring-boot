package com.example.demo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <pre>
 * 	Functional한 형태로 Given-When-Then 테스트를 작성할 수 있도록 도와주는 클래스<br>
 * 	Github given-when-then 프로젝트{@see https://github.com/gabriel-deliu/given-when-then}를 토대로 사용상의 편의성을 조금 더 보강한 클래스
 * </pre>
 */
public class GivenWhenThen<T> {

	private T fixture;

	private GivenWhenThen(T fixture) {
		this.fixture = fixture;
	}

	private GivenWhenThen(String message, Supplier<T> fixtureSupplier) {
		if (fixtureSupplier == null) {
			throw new RuntimeException(Optional.ofNullable(message).orElse("No fixture supplier is exists."));
		}

		try {
			this.fixture = fixtureSupplier.get();
		} catch (Exception e) {
			throw new RuntimeException(Optional.ofNullable(message).orElse(e.getMessage()), e);
		}
	}

	public static <T> GivenWhenThen<T> given(T receivedObj) {
		return given(null, receivedObj);
	}

	public static <T> GivenWhenThen<T> given(String message, T receivedObj) {
		return new GivenWhenThen<>(receivedObj);
	}

	public static <T> GivenWhenThen<T> given(Supplier<T> fixtureSupplier) {
		return given(null, fixtureSupplier);
	}

	public static <T> GivenWhenThen<T> given(String message, Supplier<T> fixtureSupplier) {
		return new GivenWhenThen<>(message, fixtureSupplier);
	}

	public <F> GivenWhenThen<F> when(Function<T, F> whenFunction) {
		return when(null, whenFunction);
	}

	public <F> GivenWhenThen<F> when(String message, Function<T, F> whenFunction) {
		try {
			return new GivenWhenThen<>(whenFunction.apply(fixture));
		} catch (Exception e) {
			throw new RuntimeException(Optional.ofNullable(message).orElse(e.getMessage()), e);
		}
	}

	public void then(Predicate<T> thenFunction) {
		then(null, thenFunction);
	}

	public void then(String message, Predicate<T> thenFunction) {
		boolean testResult = thenFunction.test(fixture);
		if (!testResult) {
			throw new RuntimeException(Optional.ofNullable(message).orElse("Assertion failed."));
		}
	}

	public void then(Consumer<T> thenFunction) {
		then(null, thenFunction);
	}

	public void then(String message, Consumer<T> thenFunction) {
		try {
			thenFunction.accept(fixture);
		} catch (Exception e) {
			throw new RuntimeException(Optional.ofNullable(message).orElse(e.getMessage()), e);
		}
	}
}
