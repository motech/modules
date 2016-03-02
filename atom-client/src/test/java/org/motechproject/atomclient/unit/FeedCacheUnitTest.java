package org.motechproject.atomclient.unit;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.motechproject.atomclient.service.impl.FeedCache;

import java.io.IOException;

import static org.junit.Assert.assertThat;

public class FeedCacheUnitTest {

    @Test
    public void verifyFeedSerialization() throws IOException, ClassNotFoundException, CloneNotSupportedException, FeedException {
        SyndFeed feed1 = new SyndFeedImpl();
        feed1.setFeedType("atom_1.0");
        feed1.setUri("foobar");
        String s = FeedCache.feedToString(feed1);
        SyndFeed feed2 = FeedCache.feedFromString(s);
        assertThat(feed2, IsEqual.equalTo(feed1));
    }
}
