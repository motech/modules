package org.motechproject.adherence.service;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceToken;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AdherenceTokenService extends MotechBaseRepository<AdherenceToken> {

    public static String GENERIC_TOKEN_ID = null;

    @Autowired
    protected AdherenceTokenService(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceToken.class, db);
    }

    public AdherenceToken createToken(String externalId, String referenceId) {
        AdherenceToken adherenceToken = new AdherenceToken(externalId, referenceId, DateUtil.today());
        add(adherenceToken);
        return adherenceToken;
    }

    @View(name = "find_latest_token", map = "function(doc) {if(doc.type == 'AdherenceToken') {emit([doc.externalId, doc.referenceId, doc.creationDate], doc._id);} }")
    public AdherenceToken getLatestToken(String externalId, String referenceId) {
        ViewQuery query = createQuery("find_latest_token").startKey(ComplexKey.of(externalId, referenceId, ComplexKey.emptyObject())).limit(1).descending(true).includeDocs(true);
        List<AdherenceToken> adherenceTokens = db.queryView(query, AdherenceToken.class);
        return CollectionUtils.isEmpty(adherenceTokens) ? null : adherenceTokens.get(0);
    }

    public List<AdherenceToken> getTokensBetween(String externalId, String referenceId, LocalDate fromDate, LocalDate toDate) {
        ViewQuery query = createQuery("find_latest_token").startKey(ComplexKey.of(externalId, referenceId, fromDate)).endKey(ComplexKey.of(externalId, referenceId, toDate))
                .inclusiveEnd(true).includeDocs(true);
        return db.queryView(query, AdherenceToken.class);
    }
}
