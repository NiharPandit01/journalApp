
package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.Reflection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReflectionRepository extends MongoRepository<Reflection, ObjectId> {

    Optional<Reflection> findByUserNameAndCategoryAndDate(
            String userName, String category, LocalDate date
    );

    List<Reflection> findByUserName(String userName);

    List<Reflection> findByUserNameAndCategory(
            String userName, String category
    );
}