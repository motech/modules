package org.motechproject.aggregator.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.aggregator.outbound.DeliveryTimeBasedExpiry;
import org.springframework.integration.store.MessageGroup;
import org.springframework.integration.store.MessageGroupCallback;

import javax.sql.DataSource;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageStoreTest {
    @Mock
    DeliveryTimeBasedExpiry timeBasedExpiry;
    @Mock
    DataSource dataSource;
    @Mock
    MessageGroup group;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldCallbackOnAllRegisteredCallbacksDuringExpiry() {
        MessageStore store = new MessageStore(timeBasedExpiry, dataSource);

        MessageGroupCallback firstCallBack = mock(MessageGroupCallback.class);
        MessageGroupCallback secondCallBack = mock(MessageGroupCallback.class);

        store.registerMessageGroupExpiryCallback(firstCallBack);
        store.registerMessageGroupExpiryCallback(secondCallBack);
        store.expire(group);

        verify(firstCallBack).execute(store, group);
        verify(secondCallBack).execute(store, group);
    }

    @Test
    public void shouldContinueOtherCallbacksEvenIfOneFailsButShouldFailTheExpiryAtTheEnd() {
        MessageStore store = new MessageStore(timeBasedExpiry, dataSource);

        MessageGroupCallback firstCallBack = mock(MessageGroupCallback.class);
        MessageGroupCallback secondCallBack = mock(MessageGroupCallback.class);

        store.registerMessageGroupExpiryCallback(firstCallBack);
        store.registerMessageGroupExpiryCallback(secondCallBack);

        doThrow(new IllegalArgumentException()).when(firstCallBack).execute(store, group);

        try {
            store.expire(group);
            fail("Should have thrown an exception after finishing");
        } catch (IllegalArgumentException e) {
        }

        verify(firstCallBack).execute(store, group);
        verify(secondCallBack).execute(store, group);
    }
}
