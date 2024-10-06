package net.edigest.journalApp.service;

import net.edigest.journalApp.entity.User;
import net.edigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {
            "Rutuja",
            "Tanuja",
            "Pratik"
    })
    public void testFindByUserName(String name){

//        User user = userRepository.findByUserName("Pratik");

        assertEquals(4,2+2);
        assertNotNull(userRepository.findByUserName(name));
//        assertTrue(user.getJournalEntries().isEmpty());

    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,2,4",
            "4,4,10"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a + b);
    }


}
