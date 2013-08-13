package se.slashat.slashapp.viewmodel;

public class ViewModelBase<T> {

	private T model;
    private boolean animationShown;
	
	
	public ViewModelBase(T model) {
		this.model = model;
	}
	
	public T getModel() {
		return model;
	}

    public boolean isAnimationShown() {
        return animationShown;
    }

    public void setAnimationShown(boolean animationShown) {
        this.animationShown = animationShown;
    }
}
