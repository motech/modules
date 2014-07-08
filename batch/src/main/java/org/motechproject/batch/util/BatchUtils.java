package org.motechproject.batch.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

import org.motechproject.commons.date.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class BatchUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchUtils.class);

    private BatchUtils() {

    }

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
        } catch (SocketException e) {
            LOGGER.debug(e.getMessage());
        } catch (UnknownHostException e) {
            LOGGER.debug(e.getMessage());
        }

        return hostName;
    }

    public static Date getCurrentDateTime() {
        return DateUtil.now().toDate();
    }
}
