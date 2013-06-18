package se.slashat.slashapp;

public interface Callback<T> {
	void call(T result);
}
