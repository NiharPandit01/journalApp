
package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.model.ReflectionWithJournalsResponse;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import net.engineeringdigest.journalApp.entity.Reflection;
import net.engineeringdigest.journalApp.service.ReflectionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reflections")
public class ReflectionController {

    @Autowired
    private ReflectionService reflectionService;


    // Save or update personal thoughts
    @PostMapping
    public ResponseEntity<?> saveReflection(
            @RequestBody ReflectionRequest request) {

        try {
            String userName = getLoggedInUser();

            Reflection reflection = reflectionService.saveReflection(
                    userName,
                    request.getCategory(),
                    request.getDate(),
                    request.getThoughts()
            );

            return new ResponseEntity<>(reflection, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(), HttpStatus.BAD_REQUEST
            );
        }
    }


    // Get all reflections of logged-in user
    @GetMapping
    public ResponseEntity<?> getAllReflections() {

        String userName = getLoggedInUser();

        List<Reflection> reflections =
                reflectionService.getAllReflections(userName);

        return new ResponseEntity<>(reflections, HttpStatus.OK);
    }
    @GetMapping("/with-journals/{category}/{date}")
    public ResponseEntity<?> getReflectionWithJournals(
            @PathVariable String category,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        String userName = getLoggedInUser();

        return reflectionService.getReflectionWithJournals(
                        userName, category, date
                )
                .<ResponseEntity<?>>map(response ->
                        new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get reflection by category and date
    @GetMapping("/{category}/{date}")
    public ResponseEntity<?> getReflection(
            @PathVariable String category,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String userName = getLoggedInUser();

        return reflectionService.getReflection(
                        userName, category, date
                )
                .<ResponseEntity<?>>map(
                        reflection -> new ResponseEntity<>(
                                reflection, HttpStatus.OK
                        )
                )
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Associate a journal entry with a reflection
    @PostMapping("/{reflectionId}/journals/{journalId}")
    public ResponseEntity<?> addJournalEntry(
            @PathVariable ObjectId reflectionId,
            @PathVariable ObjectId journalId) {

        try {
            String userName = getLoggedInUser();

            Reflection reflection =
                    reflectionService.addJournalEntry(
                            userName, reflectionId, journalId
                    );

            return new ResponseEntity<>(reflection, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(), HttpStatus.BAD_REQUEST
            );
        }
    }


    private String getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }


    // Request body for saving reflections
    @Data
    public static class ReflectionRequest {

        private String category;

        private LocalDate date;

        private List<String> thoughts;
    }

    @PutMapping("/{reflectionId}")
    public ResponseEntity<?> updateReflection(
            @PathVariable ObjectId reflectionId,
            @RequestBody ReflectionRequest request) {

        try {
            String userName = getLoggedInUser();

            Reflection reflection = reflectionService.updateReflection(
                    userName,
                    reflectionId,
                    request.getThoughts()
            );

            return new ResponseEntity<>(reflection, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{reflectionId}")
    public ResponseEntity<?> deleteReflection(
            @PathVariable ObjectId reflectionId) {

        try {
            String userName = getLoggedInUser();

            reflectionService.deleteReflection(userName, reflectionId);

            return new ResponseEntity<>(
                    "Reflection deleted successfully",
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getReflectionsByCategory(
            @PathVariable String category) {

        String userName = getLoggedInUser();

        List<Reflection> reflections =
                reflectionService.getReflectionsByCategory(
                        userName, category
                );

        return new ResponseEntity<>(reflections, HttpStatus.OK);
    }
}
