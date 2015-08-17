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

/**
 * This is the factory responsible for providing instances {@link CommcareFormImporter} to
 * the web layer. Importer instances are stored on a per http session basis. The instances are discarded
 * once a session becomes invalidated. This class implements the {@link HttpSessionListener} to keep track
 * of sessions being discarded (the session created event does not concern it).
 */
@Service("commcareFormImporterFactory")
public class CommcareFormImporterFactory implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormImporterFactory.class);

    @Autowired
    private EventRelay eventRelay;

    @Autowired
    private CommcareFormService formService;

    private final Map<String, CommcareFormImporter> importerMap = new HashMap<>();

    /**
     * Retrieves an importer instance for the given session. A new instance will be created if it doesn't yet exist.
     * @param session the http session for which the importer should be retrieved.
     * @return the importer instance for the session
     * @throws IllegalArgumentException if the session is null
     */
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
