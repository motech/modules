package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.report.constants.ColumnType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single CommCareHQ {@link ReportMetadataInfo} column.
 */
public class ReportMetadataColumn implements Serializable {

    private static final long serialVersionUID = - 5218432575178076712L;

    @Expose
    @SerializedName("column_id")
    private String id;

    @Expose
    @SerializedName("display")
    private String display;

    @Expose
    @SerializedName("type")
    private ColumnType type;

    public ReportMetadataColumn() { }

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
