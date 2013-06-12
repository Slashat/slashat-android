package se.slashat.slashat.viewmodel;

public class ViewModelBase<T> {
	boolean isSection = false;
	String sectionName;
	private T model;
	
	
	public ViewModelBase(T model, String sectionName) {
		super();
		this.model = model;
		if (model == null){
			this.sectionName = sectionName;
			this.isSection = true;
		}
	}

	public boolean isSection() {
		return isSection;
	}
	
	public T getModel() {
		return model;
	}
	
	public String getSectionName() {
		return sectionName;
	}
	
}
