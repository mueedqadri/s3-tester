import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DynamoDb {
    private final String tableName;
    private final String keyName;
    private final String itemName;
    private final Map<Object, Object> keyValueMap;
    private final AmazonDynamoDB client;

    public DynamoDb(String tableName, String keyName, String itemName, Map<Object, Object> itemValues){
        this.tableName = tableName;
        this.keyName = keyName;
        this.itemName = itemName;
        this.keyValueMap = itemValues;
        client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-south-1").withCredentials(new AWSStaticCredentialsProvider(AWSAuth.connect())).build();
    }

    public void start() {
        DescribeTableResult description = client.describeTable(tableName);
        Long itemCount = description.getTable().getItemCount();
        for (int index = 1 ; index <= itemCount; index ++){
            Map<String, AttributeValue> result = getItem(Integer.toString(index));
            print(result);

            String selectedKey = result.get("Name").toString().toLowerCase(Locale.ROOT).split(":")[1].split(",")[0].trim();
            String value = (String) keyValueMap.get(selectedKey);
            addNewAttribute(Integer.toString(index), itemName, value);
            System.out.println("***** AFTER UPDATE******");
            result = getItem(Integer.toString(index));
            print(result);
        }

    }

    public void addNewAttribute( String itemKeyValue, String attributeName, String attributeValue){
        HashMap<String, AttributeValue> itemKey =
                new HashMap<>();
        itemKey.put(keyName, new AttributeValue(itemKeyValue));

        HashMap<String, AttributeValueUpdate> updatedValues =
                new HashMap<>();

        String[] array1 = {attributeName, attributeValue};
        String[][] extraFields = {array1};
        for (String[] field : extraFields) {
            updatedValues.put(field[0], new AttributeValueUpdate(
                    new AttributeValue(field[1]), AttributeAction.PUT));
        }
        try {
            client.updateItem(tableName, itemKey, updatedValues);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public Map<String, AttributeValue> getItem(String itemKeyValue){
        HashMap<String, AttributeValue> keyToGet =
                new HashMap<>();
        Map<String, AttributeValue> result = null;
        keyToGet.put(keyName, new AttributeValue(itemKeyValue));
        GetItemRequest request;
        request = new GetItemRequest()
                .withKey(keyToGet)
                .withTableName(tableName);
        try {
            result = client.getItem(request).getItem();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return result;
    }

    public void print(Map<String, AttributeValue> result){
        if (result != null) {
            Set<String> keys = result.keySet();
            for (String key : keys) {
                System.out.format("%s: %s\n",
                        key, result.get(key).toString());
            }
        }
    }
}
