package se.slashat.slashat.viewmodel;

public class ViewModelBase<T> {

	private T model;
	
	
	public ViewModelBase(T model) {
		this.model = model;
	}
	
	public T getModel() {
		return model;
	}
	

	
}
