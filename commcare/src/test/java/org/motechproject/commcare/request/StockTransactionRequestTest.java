package org.motechproject.commcare.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StockTransactionRequestTest {

    @Test
    public void shouldConvertToQueryString() {
        StockTransactionRequest stockTransactionRequest = getStockTransactionRequest();
        assertEquals(stockTransactionRequest.toQueryString(),
                ("section_id=S100&case_id=C100&start_date=startdate&end_date=enddate&limit=100&offset=200"));
    }

    @Test
    public void shouldSkipEmptyQueryParams() {
        StockTransactionRequest stockTransactionRequest = getStockTransactionRequest();
        stockTransactionRequest.setCaseId(null);
        assertEquals("section_id=S100&start_date=startdate&end_date=enddate&limit=100&offset=200",
                stockTransactionRequest.toQueryString());

        stockTransactionRequest.setSectionId(null);
        assertEquals("start_date=startdate&end_date=enddate&limit=100&offset=200",
                stockTransactionRequest.toQueryString());

        stockTransactionRequest.setStartDate(null);
        assertEquals("end_date=enddate&limit=100&offset=200",
                stockTransactionRequest.toQueryString());

        stockTransactionRequest.setEndDate(null);
        assertEquals("limit=100&offset=200",
                stockTransactionRequest.toQueryString());
    }

    private StockTransactionRequest getStockTransactionRequest() {
        StockTransactionRequest stockTransactionRequest = new StockTransactionRequest();
        stockTransactionRequest.setCaseId("C100");
        stockTransactionRequest.setSectionId("S100");
        stockTransactionRequest.setStartDate("startdate");
        stockTransactionRequest.setEndDate("enddate");
        stockTransactionRequest.setLimit(100);
        stockTransactionRequest.setOffset(200);
        return stockTransactionRequest;
    }
}
