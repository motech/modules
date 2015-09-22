package org.motechproject.hub.util;

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
 * Utility class for the Hub module. Contains various methods to use with the Hub module.
 */
public final class HubUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubUtils.class);

    /**
     * Gets the network host name.
     *
     * @return the network host name
     */
    public static String getNetworkHostName() {
        String hostName = null;

        try {
            hostName = InetAddress.getLocalHost().getHostName();

            if ("localhost".equalsIgnoreCase(hostName)) {
                NetworkInterface networkInterface = NetworkInterface
                        .getByName("eth0");

                Enumeration<InetAddress> a = networkInterface
                        .getInetAddresses();

                for (; a.hasMoreElements();) {
                    InetAddress addr = a.nextElement();
                    hostName = addr.getCanonicalHostName();
                    // Check for ipv4 only
                    if (!hostName.contains(":")) {
                        break;
                    }
                }
            }

        } catch (SocketException | UnknownHostException e) {
            LOGGER.warn("Failed to fetch network host name", e);
        }

        return hostName;
    }

    /**
     * Gets the current date.
     *
     * @return current date
     */
    public static Date getCurrentDateTime() {
        return DateUtil.now().toDate();
    }

    private HubUtils() {
    }
}
