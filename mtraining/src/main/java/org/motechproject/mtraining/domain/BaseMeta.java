package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Field;

/**
 * Created by kosh on 6/2/14.
 */
public class BaseMeta {

    @Field
    private String name;

    @Field
    private boolean isActive;

    @Field
    private int sequenceNumber;
}
