
package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.Reflection;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.model.ReflectionWithJournalsResponse;
import net.engineeringdigest.journalApp.repository.ReflectionRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReflectionService {

    @Autowired
    private ReflectionRepository reflectionRepository;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // Save or update thoughts for a category and date
    public Reflection saveReflection(
            String userName,
            String category,
            LocalDate date,
            List<String> thoughts) {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }

        if (date == null) {
            date = LocalDate.now();
        }

        category = category.trim().toUpperCase();

        Optional<Reflection> existing =
                reflectionRepository.findByUserNameAndCategoryAndDate(
                        userName, category, date
                );

        Reflection reflection = existing.orElseGet(Reflection::new);

        reflection.setUserName(userName);
        reflection.setCategory(category);
        reflection.setDate(date);

        reflection.setThoughts(
                thoughts == null ? new ArrayList<>() : thoughts
        );

        if (reflection.getJournalIds() == null) {
            reflection.setJournalIds(new ArrayList<>());
        }

        return reflectionRepository.save(reflection);
    }

    // Get all reflections belonging to a user
    public List<Reflection> getAllReflections(String userName) {
        return reflectionRepository.findByUserName(userName);
    }

    // Get reflection by category and date
    public Optional<Reflection> getReflection(
            String userName,
            String category,
            LocalDate date) {

        return reflectionRepository.findByUserNameAndCategoryAndDate(
                userName, category.trim().toUpperCase(), date
        );
    }

    // Associate a journal entry with a reflection
    public Reflection addJournalEntry(
            String userName,
            ObjectId reflectionId,
            ObjectId journalId) {

        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Reflection not found"));

        // Ensure the reflection belongs to the logged-in user
        if (!reflection.getUserName().equals(userName)) {
            throw new IllegalArgumentException("Unauthorized reflection access");
        }

        // Verify that the journal belongs to the logged-in user
        User user = userService.findByUserName(userName);

        boolean ownsJournal = user.getJournalEntries().stream()
                .anyMatch(entry -> entry.getId().equals(journalId));

        if (!ownsJournal) {
            throw new IllegalArgumentException(
                    "Journal entry not found for this user"
            );
        }

        JournalEntry journalEntry = journalEntryService.findById(journalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Journal entry not found"));

        // Only associate entries from the same date
        if (journalEntry.getDate() == null ||
                !journalEntry.getDate().toLocalDate().equals(reflection.getDate())) {

            throw new IllegalArgumentException(
                    "Journal entry date does not match reflection date"
            );
        }

        if (reflection.getJournalIds() == null) {
            reflection.setJournalIds(new ArrayList<>());
        }

        if (!reflection.getJournalIds().contains(journalId)) {
            reflection.getJournalIds().add(journalId);
        }

        return reflectionRepository.save(reflection);
    }

    // Get reflection along with its associated journal entries
    public Optional<ReflectionWithJournalsResponse> getReflectionWithJournals(
            String userName,
            String category,
            LocalDate date) {

        Optional<Reflection> reflectionOptional =
                reflectionRepository.findByUserNameAndCategoryAndDate(
                        userName, category.trim().toUpperCase(), date
                );

        // Java 8 compatible check
        if (!reflectionOptional.isPresent()) {
            return Optional.empty();
        }

        Reflection reflection = reflectionOptional.get();

        // Retrieve all associated journal entries
        List<JournalEntry> journalEntries = reflection.getJournalIds()
                .stream()
                .map(journalEntryService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Optional.of(
                new ReflectionWithJournalsResponse(
                        reflection, journalEntries
                )
        );
    }

    public Reflection updateReflection(
            String userName,
            ObjectId reflectionId,
            List<String> thoughts) {

        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Reflection not found"));

        if (!reflection.getUserName().equals(userName)) {
            throw new IllegalArgumentException("Unauthorized reflection access");
        }

        reflection.setThoughts(
                thoughts == null ? new ArrayList<>() : thoughts
        );

        return reflectionRepository.save(reflection);
    }

    // Delete a reflection without deleting its journal entries
    public void deleteReflection(String userName, ObjectId reflectionId) {

        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Reflection not found"));

        // Ensure the reflection belongs to the logged-in user
        if (!reflection.getUserName().equals(userName)) {
            throw new IllegalArgumentException("Unauthorized reflection access");
        }

        reflectionRepository.delete(reflection);
    }

    // Get reflections by category
    public List<Reflection> getReflectionsByCategory(
            String userName,
            String category) {

        return reflectionRepository.findByUserNameAndCategory(
                userName,
                category.trim().toUpperCase()
        );
    }
}
