package com.example.aggregator.controllers;

import com.example.aggregator.models.User;
import com.example.aggregator.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.headers.HttpPublicKeyPinningDsl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            String registerUser = userService.registerUser(user);
            logger.info("User Register successfully username{}|userEMail{}",user.getUsername(),user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
        } catch (Exception e) {
            logger.error("Getting Error register user username{}|userEmail{}",user.getUsername(),user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
            String loginUser = userService.loginUser(username, password);
        try {
            if (loginUser !=null  && loginUser.equals(username)){
                logger.info("User login successfully usrname : {}",username);
                return ResponseEntity.status(HttpStatus.OK).body("user login successfully");
            }
            else {
                logger.warn("Invalid login attempt for username: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password please try again!");
            }
        } catch (Exception e) {
            logger.error("Failed ot login username :{}",username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to login");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        User userById = userService.getUserById(id);
        if (userById !=null){
            logger.info("user details found  userId : {}",id);
            return ResponseEntity.status(HttpStatus.OK).body(userById);
        }else {
            logger.warn("invalid user ID userid :{}",id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers ==null || allUsers.isEmpty()){
            logger.error("user details not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            logger.info("user details found");
            return ResponseEntity.status(HttpStatus.OK).body(allUsers);
        }
    }

    @DeleteMapping("/delete/{id}")
   public ResponseEntity<String>deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            logger.info("user details deleted successfully userId : {}",id);
            return ResponseEntity.status(HttpStatus.OK).body("user deleted"+id);
        } catch (Exception e) {
            logger.error("Deleting user getting error userid : {}",id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed delete the user please check userId : {}"+id);
        }
    }
}
