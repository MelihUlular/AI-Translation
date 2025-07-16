package SpringTranslateApi.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {

    @Autowired
    private Environment env;

    private static final String DB_NAME = "Translation_DB";
    private static final String COLLECTION_NAME = "ai_records";

    @Bean
    public MongoClient mongoClient() {
        String connectionString = env.getProperty("spring.data.mongodb.uri");
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoDatabase database(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        checkAndCreateCollection(database);
        return database;
    }

    @Bean
    public MongoCollection<Document> aiRecordsCollection(MongoDatabase database) {
        return database.getCollection(COLLECTION_NAME);
    }

    private void checkAndCreateCollection(MongoDatabase database) {
        List<String> collections = database.listCollectionNames().into(new ArrayList<>());
        if (!collections.contains(COLLECTION_NAME)) {
            database.createCollection(COLLECTION_NAME);
            System.out.println("Collection '" + COLLECTION_NAME + "' created.");
        }
    }
}