package org.motechproject.mtraining.domain;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
public class Module extends BaseMeta {

    private List<Chapter> chapters;

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
