package se.slashat.slashat;

public interface Callback<T> {
	void call(T result);
}
