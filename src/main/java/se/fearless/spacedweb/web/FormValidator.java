package se.fearless.spacedweb.web;

import org.springframework.validation.Errors;

public interface FormValidator<T> {
	public void validate(T formData, Errors errors);
}
