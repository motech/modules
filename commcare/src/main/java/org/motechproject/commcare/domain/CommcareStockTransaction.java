package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single CommCare stock transaction.
 */
public class CommcareStockTransaction {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;

    private double quantity;

    @SerializedName("section_id")
    private String sectionId;

    @SerializedName("stock_on_hand")
    private double stockOnHand;

    @SerializedName("transaction_date")
    private String transactionDate;

    private String type;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public double getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(double stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
