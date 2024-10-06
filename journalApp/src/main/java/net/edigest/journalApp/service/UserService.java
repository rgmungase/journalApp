package net.edigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.edigest.journalApp.entity.JournalEntry;
import net.edigest.journalApp.entity.User;
import net.edigest.journalApp.repository.JournalEntryRepository;
import net.edigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        } catch (Exception e) {

            log.info("error occurred for {} : ", user.getUserName());
            log.warn("error occurred for {} : ", user.getUserName());
            log.error("error occurred for {} : ", user.getUserName());
            log.trace("error occurred for {} : ", user.getUserName());
            log.debug("error occurred for {} : ", user.getUserName());

            System.out.println(e.getMessage());
        }
    }

    public void saveAdminUser(User user) {
        try {
            user.setRoles(Arrays.asList("USER","ADMIN"));
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveNewUser(User user) {
            userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
