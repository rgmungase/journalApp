package net.edigest.journalApp.controller;

import net.edigest.journalApp.entity.User;
import net.edigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getUsers() {
        List<User> allUsers = userService.getAllUsers();
        if(allUsers!=null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    @PostMapping("/create-admin-user")
    //    public ResponseEntity<?> createAdminUser(@RequestBody User user) {
    //        userService.saveAdminUser(user);
    //        return new ResponseEntity<>(HttpStatus.OK);
    //    }
}
