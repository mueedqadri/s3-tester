import java.util.HashMap;
import java.util.Map;

public class PartC {
    public static void main(String args[]){
        Map<Object, Object> keyValuePair = new HashMap<>();
        keyValuePair.put("long valley caldera", "");
        keyValuePair.put("valles caldera", "");
        keyValuePair.put("taupo caldera", "");
        keyValuePair.put("lake toba", "75,000 years ago");
        keyValuePair.put("aira caldera", "");
        keyValuePair.put("yellowstone caldera", "640,000 years ago");
        DynamoDb dynamoDb = new DynamoDb("Super_Volcano",
                "id",
                "last_eruption_period",
                keyValuePair);
        dynamoDb.start();
    }
}
