package net.engineeringdigest.journalApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.Reflection;

import java.util.List;

@Data
@AllArgsConstructor
public class ReflectionWithJournalsResponse {

    private Reflection reflection;
    private List<JournalEntry> journalEntries;
}