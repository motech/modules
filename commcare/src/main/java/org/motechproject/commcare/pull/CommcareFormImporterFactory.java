package org.motechproject.commcare.pull;


import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

@Service("commcareFormImporterFactory")
public class CommcareFormImporterFactory implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormImporterFactory.class);

    @Autowired
    private EventRelay eventRelay;

    @Autowired
    private CommcareFormService formService;

    private final Map<String, CommcareFormImporter> importerMap = new HashMap<>();

    public CommcareFormImporter getImporter(HttpSession session) {
        if (session == null) {
            throw new IllegalArgumentException("No session provided, importers must be tied with an HTTP session");
        }

        final String sid = session.getId();

        LOGGER.debug("Retrieving importer for session with ID: {}", sid);

        if (!importerMap.containsKey(sid)) {
            LOGGER.debug("No importer available for session with ID: {}. Creating a new one.", sid);
            importerMap.put(sid, new CommcareFormImporterImpl(eventRelay, formService));
        }

        return importerMap.get(sid);
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.trace("Received session created event, id: {}", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        final String sid = se.getSession().getId();
        LOGGER.debug("Received sessionDestroyed event, id: {}", sid);

        CommcareFormImporter removedImporter = importerMap.remove(sid);

        if (removedImporter == null) {
            LOGGER.debug("No importer stored for session with id: {}", sid);
        } else {
            LOGGER.debug("Discarding importer for session with id: {}, import in progress: {}",
                    sid, removedImporter.isImportInProgress());
        }
    }
}
