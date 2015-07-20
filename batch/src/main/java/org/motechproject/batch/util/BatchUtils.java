package org.motechproject.batch.util;

import org.motechproject.commons.date.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

/**
 * Batch module utilities.
 */
public final class BatchUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchUtils.class);

    /**
     * Returns the network host for this computer. Will try to get the network IP
     * from eth0, if localhost is simply set as the local host name.
     * @return the host name
     */
    public static String getNetworkHostName() {
        String hostName = null;

        try {
            hostName = InetAddress.getLocalHost().getHostName();

            if ("localhost".equalsIgnoreCase(hostName)) {
                NetworkInterface networkInterface = NetworkInterface.getByName("eth0");

                if (networkInterface == null) {
                    return hostName;
                }
                Enumeration<InetAddress> a = networkInterface
                        .getInetAddresses();

                while (a.hasMoreElements()) {
                    InetAddress addr = a.nextElement();
                    hostName = addr.getCanonicalHostName();
                    // Check for ipv4 only
                    if (!hostName.contains(":")) {
                        break;
                    }
                }
            }
        } catch (SocketException | UnknownHostException e) {
            LOGGER.error("Unable to get the host name", e);
        }

        return hostName;
    }

    /**
     * Retrieves the current date and time as a {@link Date}.
     * @return the date representing current time
     */
    public static Date getCurrentDateTime() {
        return DateUtil.now().toDate();
    }

    private BatchUtils() {
    }
}
