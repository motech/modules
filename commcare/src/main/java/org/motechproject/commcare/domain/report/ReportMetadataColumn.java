package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.report.constants.ColumnType;

/**
 * Represents a single CommCareHQ {@link ReportMetadataInfo} column.
 */
public class ReportMetadataColumn {

    @SerializedName("column_id")
    private String id;

    @SerializedName("display")
    private String display;

    @SerializedName("type")
    private ColumnType type;

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

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }
}