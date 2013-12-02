package org.motechproject.sms.audit;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import org.codehaus.jackson.type.TypeReference;
import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.lucene.query.CouchDbLuceneQuery;
import org.motechproject.commons.couchdb.query.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Used to query the audit records in the database
 */
@Repository
public class AllSmsRecords extends CouchDbRepositorySupportWithLucene<SmsRecord> {

    private final Logger logger = LoggerFactory.getLogger(AllSmsRecords.class);

    @FullText({@Index(
            name = "search",
            index = "function(doc) { " +
                    "var result=new Document(); " +
                    "result.add(doc.smsDirection,{'field':'smsDirection'}); " +
                    "result.add(doc.config, {'field':'config'});" +
                    "result.add(doc.phoneNumber, {'field':'phoneNumber'});" +
                    "result.add(doc.messageContent, {'field':'messageContent'}); " +
                    "result.add(doc.timestamp,{'field':'timestamp', 'type':'date'}); " +
                    "result.add(doc.providerStatus, {'field':'providerStatus'}); " +
                    "result.add(doc.deliveryStatus, {'field':'deliveryStatus'}); " +
                    "result.add(doc.motechId, {'field':'motechId'}); " +
                    "result.add(doc.providerId, {'field':'providerId'}); " +
                    "result.add(doc.errorMessage, {'field':'errorMessage'}); " +
                    "return result " +
                    "}"
    ) })

    public SmsRecords findAllBy(SmsRecordSearchCriteria criteria) {
        StringBuilder query = new CouchDbLuceneQuery()
                .withAny("smsDirection", criteria.getSmsDirections())
                .with("config", criteria.getConfig())
                .with("phoneNumber", criteria.getPhoneNumber())
                .with("messageContent", criteria.getMessageContent())
                .withDateRange("timestamp", criteria.getTimestampRange())
                .withAny("deliveryStatus", criteria.getDeliveryStatuses())
                .with("providerStatus", criteria.getProviderStatus())
                .with("motechId", criteria.getMotechId())
                .with("providerId", criteria.getProviderId())
                .with("errorMessage", criteria.getErrorMessage())
                .build();
        return runQuery(query, criteria.getQueryParam());
    }

    private SmsRecords runQuery(StringBuilder queryString, QueryParam queryParam) {
        LuceneQuery query = new LuceneQuery("SmsRecord", "search");
        query.setQuery(queryString.toString());
        query.setStaleOk(false);
        query.setIncludeDocs(true);
        int recordsPerPage = queryParam.getRecordsPerPage();
        if (recordsPerPage > 0) {
            query.setLimit(recordsPerPage);
            query.setSkip(queryParam.getPageNumber() * recordsPerPage);
        }
        String sortBy = queryParam.getSortBy();
        if (isNotBlank(sortBy)) {
            Class clazz = SmsRecord.class;
            try {
                Field f = clazz.getDeclaredField(sortBy);
                if (f.getType().equals(DateTime.class)) {
                    sortBy = sortBy + "<date>";
                }
            } catch (NoSuchFieldException e) {
                logger.error(String.format("No found field %s", sortBy), e);
            }
            String sortString = queryParam.isReverse() ? "\\" + sortBy : sortBy;
            query.setSort(sortString);
        }
        TypeReference<CustomLuceneResult<SmsRecord>> typeRef
                = new TypeReference<CustomLuceneResult<SmsRecord>>() {
        };
        return convertToSmsRecords(db.queryLucene(query, typeRef));
    }

    private SmsRecords convertToSmsRecords(CustomLuceneResult<SmsRecord> result) {
        List<SmsRecord> smsRecords = new ArrayList<>();
        int count = 0;
        if (result != null) {
            List<CustomLuceneResult.Row<SmsRecord>> rows = result.getRows();
            for (CustomLuceneResult.Row<SmsRecord> row : rows) {
                smsRecords.add(row.getDoc());
            }
            count = result.getTotalRows();
        }
        return new SmsRecords(count, smsRecords);
    }

    @Autowired
    protected AllSmsRecords(@Qualifier("smsDBConnector") CouchDbConnector db) throws IOException {
        super(SmsRecord.class, new LuceneAwareCouchDbConnector(db.getDatabaseName(), new StdCouchDbInstance(db.getConnection())));
        initStandardDesignDocument();
    }
}
