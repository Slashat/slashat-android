package se.slashat.slashapp.viewmodel;

import se.slashat.slashapp.model.SectionModel;

public class SectionViewModel extends DisplayRowViewModel<SectionModel> {

    private final String sectionName;

    public SectionViewModel(SectionModel sectionModel) {
		super(sectionModel);
        sectionName = sectionModel.getSectionName();
    }

    public String getSectionName() {
        return sectionName;
    }
}
