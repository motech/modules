package org.motechproject.commcare.domain.report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringEscapeUtils;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;
import org.motechproject.mds.util.SecurityMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jdo.annotations.NotPersistent;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper class for storing list of instances of the {@link ReportMetadataInfo} class. It's a part of the MOTECH model.
 */
@Entity(name = "Commcare Reports Metadata")
@CrudEvents(CrudEventType.NONE)
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCommcare"})
public class ReportsMetadataInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsMetadataInfo.class);

    @Ignore
    @NotPersistent
    @SerializedName("objects")
    private List<ReportMetadataInfo> reportMetadataInfoList;

    @Ignore
    @NotPersistent
    @SerializedName("meta")
    private CommcareMetadataInfo metadataInfo;

    @Field(displayName = "Source configuration")
    private String configName;

    @Field(displayName = "Metadata", type = "text")
    private String serializedReportMetadataInfoList;

    @Field(displayName = "Reports", type = "text")
    private String serializedMetadataInfo;

    public ReportsMetadataInfo() { }

    public ReportsMetadataInfo(List<ReportMetadataInfo> reportMetadataInfoList, CommcareMetadataInfo metadataInfo) {
        this.reportMetadataInfoList = reportMetadataInfoList;
        this.metadataInfo = metadataInfo;
        serializeMetadataInfo();
        serializeReportMetadataInfoList();
    }

    public List<ReportMetadataInfo> getReportMetadataInfoList() {
        if (reportMetadataInfoList == null && serializedReportMetadataInfoList != null) {
            deserializeReportMetadataInfoList();
        }
        return reportMetadataInfoList;
    }

    public void setReportMetadataInfoList(List<ReportMetadataInfo> reportMetadataInfoList) {
        this.reportMetadataInfoList = reportMetadataInfoList;
        serializeReportMetadataInfoList();
    }

    public CommcareMetadataInfo getMetadataInfo() {
        if (metadataInfo == null && serializedMetadataInfo != null) {
            deserializeMetadataInfo();
        }
        return metadataInfo;
    }

    public void setMetadataInfo(CommcareMetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
        serializeMetadataInfo();
    }

    public String getConfigName () {
        return configName;
    }

    public void setConfigName (String configName) {
        this.configName = configName;
    }

    public String getSerializedReportMetadataInfoList () {
        if (serializedReportMetadataInfoList == null && reportMetadataInfoList != null) {
            serializeReportMetadataInfoList();
        }
        return serializedReportMetadataInfoList;
    }

    public void setSerializedReportMetadataInfoList (String serializedReportMetadataInfoList) {
        this.serializedReportMetadataInfoList = serializedReportMetadataInfoList;
        deserializeReportMetadataInfoList();
    }

    public String getSerializedMetadataInfo () {
        if (serializedMetadataInfo == null && metadataInfo != null) {
            serializeMetadataInfo();
        }
        return serializedMetadataInfo;
    }

    public void setSerializedMetadataInfo (String serializedMetadataInfo) {
        this.serializedMetadataInfo = serializedMetadataInfo;
        serializeMetadataInfo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportMetadataInfoList, metadataInfo, serializedMetadataInfo, serializedReportMetadataInfoList, configName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportsMetadataInfo)) {
            return false;
        }

        ReportsMetadataInfo other = (ReportsMetadataInfo) o;

        return Objects.equals(reportMetadataInfoList, other.reportMetadataInfoList)
                && Objects.equals(metadataInfo, other.metadataInfo)
                && Objects.equals(serializedMetadataInfo, other.serializedMetadataInfo)
                && Objects.equals(serializedReportMetadataInfoList, other.serializedReportMetadataInfoList)
                && Objects.equals(configName, other.configName);
    }

    private void serializeMetadataInfo () {
        if (metadataInfo != null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            serializedMetadataInfo = StringEscapeUtils.escapeJava(gson.toJson(metadataInfo));
        }
    }

    private void deserializeMetadataInfo () {
        if (serializedMetadataInfo != null) {
            Type deserializeType = new TypeToken<CommcareMetadataInfo>() { } .getType();
            MotechJsonReader motechJsonReader = new MotechJsonReader();

            try {
                metadataInfo = (CommcareMetadataInfo) motechJsonReader.readFromStringOnlyExposeAnnotations(
                        StringEscapeUtils.unescapeJava(serializedMetadataInfo), deserializeType);
            } catch (JsonParseException e) {
                LOGGER.error("Failed to deserialize CommCare schema from its JSON representation in the database. Fix the errors in the schema or force Commcare module to download the fresh schema.", e);
                metadataInfo = null;
            }
        }
    }

    private void serializeReportMetadataInfoList () {
        if (reportMetadataInfoList != null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            serializedReportMetadataInfoList = StringEscapeUtils.escapeJava(gson.toJson(reportMetadataInfoList));
        }
    }

    private void deserializeReportMetadataInfoList () {
        if (serializedReportMetadataInfoList != null) {
            Type deserializeType = new TypeToken<List<ReportMetadataInfo>>() { } .getType();
            MotechJsonReader motechJsonReader = new MotechJsonReader();

            try {
                reportMetadataInfoList = (List<ReportMetadataInfo>) motechJsonReader.readFromStringOnlyExposeAnnotations(
                        StringEscapeUtils.unescapeJava(serializedReportMetadataInfoList), deserializeType);
            } catch (JsonParseException e) {
                LOGGER.error("Failed to deserialize CommCare schema from its JSON representation in the database. Fix the errors in the schema or force Commcare module to download the fresh schema.", e);
                reportMetadataInfoList = Collections.emptyList();
            }
        }
    }
}
