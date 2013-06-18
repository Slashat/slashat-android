package se.slashat.slashapp.viewmodel;

public class ViewModelBase<T> {

	private T model;
	
	
	public ViewModelBase(T model) {
		this.model = model;
	}
	
	public T getModel() {
		return model;
	}
	

	
}
