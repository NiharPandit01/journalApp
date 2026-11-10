package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class  JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
 private   UserService userService;





    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
     try{
         User user = userService.findByUserName(userName);
         journalEntry.setDate(LocalDateTime.now());
         JournalEntry saved = journalEntryRepository.save(journalEntry);
         user.getJournalEntries().add(saved);
         userService.saveUser(user);

     }catch (Exception e){

         throw new RuntimeException("An error occured while saving the entry",e);
     }
    }
    public void saveEntry(JournalEntry journalEntry) {

        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed =false;
       try {
           User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x ->x.getId().equals(id));
           if(removed){
               userService.saveUser(user);
               journalEntryRepository.deleteById(id);}
           return removed;
       } catch (Exception e){
           log.error("Error",e);
       throw new RuntimeException("An error occured while deleting the entry",e);}

    }



    public List<JournalEntry> findByUserName(String username){
return null;
    }

    @Transactional
    public Optional<JournalEntry> updateEntry(
            ObjectId id,
            String userName,
            JournalEntry updatedEntry
    ) {
        User user = userService.findByUserName(userName);

        if (user == null || user.getJournalEntries() == null) {
            return Optional.empty();
        }

        Optional<JournalEntry> existingEntry = user.getJournalEntries()
                .stream()
                .filter(entry -> entry.getId().equals(id))
                .findFirst();

        if (!existingEntry.isPresent()) {
            return Optional.empty();
        }

        JournalEntry oldEntry = existingEntry.get();

        if (updatedEntry.getTitle() != null &&
                !updatedEntry.getTitle().trim().isEmpty()) {
            oldEntry.setTitle(updatedEntry.getTitle().trim());
        }

        if (updatedEntry.getContent() != null &&
                !updatedEntry.getContent().trim().isEmpty()) {
            oldEntry.setContent(updatedEntry.getContent().trim());
        }

        if (updatedEntry.getSentiment() != null) {
            oldEntry.setSentiment(updatedEntry.getSentiment());
        }

        // Update the individual journal document.
        journalEntryRepository.save(oldEntry);

        // Update the journal entry embedded in the user's document.
        userService.saveUser(user);

        return Optional.of(oldEntry);
    }
}
