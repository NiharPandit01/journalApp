package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryimplTests {
    @Autowired
    private UserRepositoryimpl userRepositoryimpl;
    @Test
    public void testSaveNewUser(){
        userRepositoryimpl.getUserForSA();

    }
}
