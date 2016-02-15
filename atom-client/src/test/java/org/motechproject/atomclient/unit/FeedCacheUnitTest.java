package org.motechproject.atomclient.unit;

import com.rometools.fetcher.impl.SyndFeedInfo;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.motechproject.atomclient.service.impl.FeedCache;

import java.io.IOException;

import static org.junit.Assert.assertThat;

public class FeedCacheUnitTest {

    @Test
    public void verifyInfoToBytes() throws IOException, ClassNotFoundException, CloneNotSupportedException {
        SyndFeedInfo info = new SyndFeedInfo();
        info.setId("foo");
        byte[] bytes = FeedCache.infoToBytes(info);
        info = FeedCache.infoFromBytes(bytes);
        assertThat(info.getId(), IsEqual.equalTo("foo"));
    }
}
