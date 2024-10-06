package net.edigest.journalApp.controller;

import net.edigest.journalApp.entity.JournalEntry;
import net.edigest.journalApp.entity.User;
import net.edigest.journalApp.service.JournalEntryService;
import net.edigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    //methods in the controller should be public
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(authentication.getName());
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries!=null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            journalEntryService.saveEntry(journalEntry,authentication.getName());
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //used whe the id is given in the path that is localhost:8080/journal/id/2
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(authentication.getName());
        List<JournalEntry> list = user.getJournalEntries().stream().filter(j -> j.getId().equals(myId)).toList();
        if(!list.isEmpty()) {
            Optional<JournalEntry> jounalEntry = journalEntryService.getById(list.get(0).getId());
            if (jounalEntry.isPresent()) {
                return new ResponseEntity<>(jounalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(authentication.getName());
        boolean removed = false;
        List<JournalEntry> list = user.getJournalEntries().stream().filter(j -> j.getId().equals(myId)).toList();
        if(!list.isEmpty()) {
            removed = journalEntryService.deleteById(authentication.getName(), list.get(0).getId());
        }
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry updatedJournalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(authentication.getName());

        List<JournalEntry> list = user.getJournalEntries().stream().filter(j -> j.getId().equals(myId)).toList();

        if(!list.isEmpty()) {
            Optional<JournalEntry> myEntry = journalEntryService.getById(list.get(0).getId());
            if (myEntry != null && myEntry.isPresent()) {
                myEntry.get().setTitle(updatedJournalEntry.getTitle() != null && !updatedJournalEntry.getTitle().equals("") ? updatedJournalEntry.getTitle() : myEntry.get().getTitle());
                myEntry.get().setContent(updatedJournalEntry.getContent() != null && !updatedJournalEntry.getContent().equals("") ? updatedJournalEntry.getContent() : myEntry.get().getContent());
                journalEntryService.saveEntry(myEntry.get());
                return new ResponseEntity<>(myEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
