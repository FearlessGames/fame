package se.fearless.spacedweb.services;

public class IllegalTokenException extends Throwable {
	public IllegalTokenException(String token) {
		super(token);
	}
}
