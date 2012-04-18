package org.motechproject.adherence.repository;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceLog.class, db);
    }

    public synchronized void insert(AdherenceLog logToInsert) {
        AdherenceLog latestLog = findLatestLog(logToInsert.externalId(), logToInsert.referenceId());
        if (latestLog == null) {
            add(logToInsert);
        } else if (!latestLog.encloses(logToInsert)) {
            add(logToInsert.alignWith(latestLog));
        }
    }

    @View(
            name = "find_by_date",
            map = "  function(doc) {" +
                    "   if(doc.type == 'AdherenceLog') {" +
                    "      emit([doc.externalId, doc.concept, doc.toDate], doc._id);" +
                    "   } " +
                    "}"
    )
    public AdherenceLog adherenceLogWith(String externalId, String conceptId, DateTime pointInTime) {
        ViewQuery query = createQuery("find_by_date")
                .startKey(ComplexKey.of(externalId, conceptId, pointInTime))
                .limit(1)
                .includeDocs(true);
        return singleResult(db.queryView(query, AdherenceLog.class));
    }

    @View(
            name = "find_all_between_date_range",
            map = "function(doc) {" +
                    "  if(doc.type == 'AdherenceLog') {" +
                    "     emit([doc.externalId, doc.concept, doc.fromDate], doc);" +
                    "     emit([doc.externalId, doc.concept, doc.toDate], doc);" +
                    "  } " +
                    "}",
            reduce =
                    " function uniqueArrayValues(o){" +
                            "  var items = o," +
                            "  ids=[];" +
                            "  output = [];" +
                            "  function check(val){" +
                            "    return ids.indexOf(val._id) === -1;" +
                            "  }" +
                            "  for(var i=0; i<items.length; i++){" +
                            "    if(check(items[i])){" +
                            "     output.push(items[i]);" +
                            "     ids.push(items[i]._id);" +
                            "    }    " +
                            "  }" +
                            "  return output;" +
                            "}" +
                            "function(keys,values){" +
                            "  values=uniqueArrayValues(values);" +
                            "  for(var j in values){" +
                            "    var summary={};" +
                            "    summary.dosesTaken=values[j].dosesTaken;" +
                            "    summary.totalDoses=values[j].totalDoses;" +
                            "    summary._id=values[j]._id;" +
                            "    values[j]=summary;" +
                            "  }" +
                            "  return values;" +
                            "}"
    )
    public List<AdherenceLog> adherenceLogsWith(String externalId, String conceptId, DateTime start, DateTime end) {
        try {
            ViewQuery query = createQuery("find_all_between_date_range")
                    .startKey(ComplexKey.of(externalId, conceptId, start))
                    .endKey(ComplexKey.of(externalId, conceptId, end))
                    .groupLevel(1)
                    .includeDocs(false)
                    .reduce(true)
                    .inclusiveEnd(true);
            return parse(query);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private AdherenceLog findLatestLog(String externalId, String referenceId) {
        ViewQuery query = createQuery("find_by_date")
                .startKey(ComplexKey.of(externalId, referenceId, null))
                .endKey(ComplexKey.of(externalId, referenceId, ComplexKey.emptyObject()))
                .limit(1)
                .descending(true)
                .includeDocs(true);
        return singleResult(db.queryView(query, AdherenceLog.class));
    }

    private List<AdherenceLog> parse(ViewQuery query) throws IOException {
        ViewResult viewResult = db.queryView(query);
        List<AdherenceLog> logs = new ArrayList<AdherenceLog>();
        ObjectMapper mapper = new ObjectMapper();
        for (ViewResult.Row row : viewResult.getRows()) {
            JsonNode values = row.getValueAsNode();
            for (int i = 0; i < values.size(); i++) {
                logs.add(mapper.readValue(values.get(i), AdherenceLog.class));
            }
        }
        return logs;
    }
}
