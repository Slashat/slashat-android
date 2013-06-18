package se.slashat.slashapp;

//TODO possible replace with JavaTuple callbacks instead.
public interface CallbackPair<T, PT> {
	void call(T result, PT pairResult);
}
