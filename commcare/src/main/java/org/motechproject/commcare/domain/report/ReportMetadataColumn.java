package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.report.constants.ColumnType;

import java.util.Objects;

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

    public ReportMetadataColumn() {}

    public ReportMetadataColumn(String id, String display, ColumnType type) {
        this.id = id;
        this.display = display;
        this.type = type;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, display, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportMetadataColumn)) {
            return false;
        }

        ReportMetadataColumn other = (ReportMetadataColumn) o;

        return Objects.equals(id, other.id) && Objects.equals(display, other.display) && Objects.equals(type, other.type);
    }
}