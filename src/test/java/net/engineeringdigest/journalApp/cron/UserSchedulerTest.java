package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserSchedulerTest {

    @Autowired
    private UserScheduler userScheduler;

    @Test
    void testFetchUsersAndSendSaMail() {
        userScheduler.fetchUsersAndSendSaMail();
    }
}