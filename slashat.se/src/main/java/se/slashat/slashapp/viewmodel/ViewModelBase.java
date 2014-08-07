package se.slashat.slashapp.viewmodel;

public class ViewModelBase<T> {

    private boolean animationShown;

    public boolean isAnimationShown() {
        return animationShown;
    }

    public void setAnimationShown(boolean animationShown) {
        this.animationShown = animationShown;
    }
}
