package SpringTranslateApi.repository;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import SpringTranslateApi.exception.TranslationServiceException;
import SpringTranslateApi.model.dto.AIRecord;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AIRecordRepository {

    private final MongoCollection<Document> collection;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    public AIRecordRepository(MongoCollection<Document> aiRecordsCollection) {
        this.collection = aiRecordsCollection;
    }

    public void insertRecord(AIRecord record) {
        String formattedDateTime = LocalDateTime.now().format(formatter);
        
        Document doc = new Document("source", record.getSource())
                .append("target", record.getTarget())
                .append("input", record.getInput())
                .append("output", record.getOutput())
                .append("created_at", formattedDateTime);
        
        try {
            collection.insertOne(doc);
        } catch (MongoException e) {
            throw new TranslationServiceException("MongoDB insert failed", e);
        }
    }

    public List<AIRecord> findAllRecords() {
        List<AIRecord> records = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        
        for (Document doc : documents) {
            AIRecord record = new AIRecord(
                doc.getString("source"),
                doc.getString("target"),
                doc.getString("input"),
                doc.getString("output")
            );
            records.add(record);
        }
        
        return records;
    }

    public void printAllRecords() {
        FindIterable<Document> records = collection.find();
        for (Document doc : records) {
            System.out.println(doc.toJson());
        }
    }
}