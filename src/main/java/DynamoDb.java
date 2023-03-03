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
    }

}
