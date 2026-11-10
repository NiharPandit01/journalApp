package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "reflections")
@Data
@NoArgsConstructor
public class Reflection {

    @Id
    private ObjectId id;

    private String userName;

    private String category;

    private LocalDate date;

    private List<String> thoughts = new ArrayList<>();

    private List<ObjectId> journalIds = new ArrayList<>();
}

