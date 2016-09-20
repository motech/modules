package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single CommCareHQ {@link ReportInfo} column.
 */
public class Column {

    @SerializedName("column_id")
    private String id;

    @SerializedName("display")
    private String display;

    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}