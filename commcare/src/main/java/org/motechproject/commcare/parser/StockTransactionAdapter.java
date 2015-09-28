package org.motechproject.commcare.parser;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commons.api.json.MotechJsonReader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for deserializing stock transactions from JSON.
 */
public final class StockTransactionAdapter {

    private static final MotechJsonReader READER = new MotechJsonReader();
    private static Map<Type, Object> providedAdapters = new HashMap<>();

    static {
        providedAdapters.put(MetadataValue.class, new MetadataValueAdapter());
    }

    /**
     * Deserializes the given JSON string into an instance of the {@link CommcareStockTransactionList} class.
     *
     * @param json  the JSON representation of the list of stock transaction
     * @return the deserialized list of stock transactions
     */
    public static CommcareStockTransactionList readListJson(String json) {
        Type type = new TypeToken<CommcareStockTransactionList>() {}.getType();
        return (CommcareStockTransactionList) READER.readFromString(json, type, providedAdapters);
    }

    /**
     * Utility class, should not be initiated.
     */
    private StockTransactionAdapter() {
    }
}
