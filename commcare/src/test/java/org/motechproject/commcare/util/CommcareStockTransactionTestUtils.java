package org.motechproject.commcare.util;

import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.domain.CommcareStockTransaction;
import org.motechproject.commcare.domain.CommcareStockTransactionList;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Utility class for preparing stock transactions and asserting equality between them.
 */
public class CommcareStockTransactionTestUtils {

    /**
     * Creates a sample list of transactions.
     *
     * @return the list of transactions
     */
    public static CommcareStockTransactionList prepareStockTransactionsList() {
        CommcareStockTransactionList list = new CommcareStockTransactionList();

        CommcareMetadataJson metadata = new CommcareMetadataJson();
        metadata.setLimit(20);
        metadata.setNextPageQueryString("?case_id=c1&limit=20&section_id=s1&offset=20");
        metadata.setOffset(0);
        metadata.setTotalCount(0);
        metadata.setPreviousPageQueryString(null);
        list.setMeta(metadata);

        list.setObjects(prepareStockTransactions());

        return list;
    }

    /**
     * Creates a list of sample transactions.
     *
     * @return the list of transactions
     */
    public static List<CommcareStockTransaction> prepareStockTransactions() {

        List<CommcareStockTransaction> transactions = new ArrayList<>();

        CommcareStockTransaction transaction = new CommcareStockTransaction();
        transaction.setProductId("p1");
        transaction.setProductName(null);
        transaction.setQuantity(0);
        transaction.setSectionId("s1");
        transaction.setStockOnHand(13);
        transaction.setTransactionDate("2015-08-10T14:59:55.029219");
        transaction.setType("soh");
        transactions.add(transaction);

        transaction = new CommcareStockTransaction();
        transaction.setProductId("p3");
        transaction.setProductName(null);
        transaction.setQuantity(0);
        transaction.setSectionId("s1");
        transaction.setStockOnHand(17);
        transaction.setTransactionDate("2015-08-10T14:59:55.029219");
        transaction.setType("soh");
        transactions.add(transaction);

        return transactions;
    }

    /**
     * Asserts that the given instances of the {@link CommcareStockTransactionList} class are equal.
     *
     * @param expected  the expected instance
     * @param actual  the actual instance
     */
    public static void assertStockTransactionListsEqual(CommcareStockTransactionList expected,
                                                  CommcareStockTransactionList actual) {
        assertMetadatasEqual(expected.getMeta(), actual.getMeta());
        assertStockTransactionsEqual(expected.getObjects(), actual.getObjects());
    }

    /**
     * Asserts that the given list of instances of the {@link CommcareStockTransaction} class are equal.
     *
     * @param expected  the list of expected instances
     * @param actual  the list of actual instances
     */
    public static void assertStockTransactionsEqual(List<CommcareStockTransaction> expected,
                                                    List<CommcareStockTransaction> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertStockTransactionsEqual(expected.get(i), actual.get(i));
        }
    }

    /**
     * Asserts that the given instances of the {@link CommcareStockTransaction} class are equal.
     *
     * @param expected  the expected instance
     * @param actual  the actual instance
     */
    public static void assertStockTransactionsEqual(CommcareStockTransaction expected, CommcareStockTransaction actual) {
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getSectionId(), actual.getSectionId());
        assertEquals(expected.getStockOnHand(), actual.getStockOnHand());
        assertEquals(expected.getTransactionDate(), actual.getTransactionDate());
        assertEquals(expected.getType(), actual.getType());
    }

    /**
     * Asserts that the given instances of the {@link CommcareMetadataJson} class are equal.
     *
     * @param expected  the expected instance
     * @param actual  the actual instance
     */
    public static void assertMetadatasEqual(CommcareMetadataJson expected, CommcareMetadataJson actual) {
        assertEquals(expected.getLimit(), actual.getLimit());
        assertEquals(expected.getNextPageQueryString(), actual.getNextPageQueryString());
        assertEquals(expected.getOffset(), actual.getOffset());
        assertEquals(expected.getPreviousPageQueryString(), actual.getPreviousPageQueryString());
        assertEquals(expected.getTotalCount(), actual.getTotalCount());
    }

    /**
     * Utility class, should not be initiated.
     */
    private CommcareStockTransactionTestUtils() {
    }
}
