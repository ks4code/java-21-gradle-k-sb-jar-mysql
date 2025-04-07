package com.sahajanand.java_21_gradle_k_sb_jar_mysql.Controller;

import com.sahajanand.java_21_gradle_k_sb_jar_mysql.model.User;
import com.sahajanand.java_21_gradle_k_sb_jar_mysql.model.UserMapper;
import com.sahajanand.java_21_gradle_k_sb_jar_mysql.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class LoginSignUpForgotPassResetPass {

  private final Map<String, User> users = new HashMap<>();
  private final UserMapper userMapper;

  public LoginSignUpForgotPassResetPass(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @GetMapping("/get-all-users")
  public List<User> getAllUsers() {
    return new ArrayList<>(users.values());
  }

  @GetMapping("/get-user-details/{userId}")
  public ResponseEntity<User> getUserDetails(@PathVariable String userId) {
    //    Utils.systemOutPrint("userId *****", userId);

    User user = users.get(userId);
    Utils.systemOutPrint("user *****", user);

    if (user != null) {
      return ResponseEntity.ok(user);
    }
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody User user) {
    Utils.systemOutPrint("user *****", user);

    for (User usetTemp : users.values()) {
      if (usetTemp.getEmail().equals(user.getEmail()) && usetTemp.getPassword().equals(user.getPassword())) {
        return ResponseEntity.ok(usetTemp);
      }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
  }

  @PostMapping("/signup")
  public ResponseEntity<User> signUp(@RequestBody User user) {
    String userId = String.valueOf(UUID.randomUUID());
    user.setId(userId);
    users.put(userId, user);
    Utils.systemOutPrint("user *****", user);
    //    return user;
    return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(user);
  }

  @PutMapping("update-user-details/{userId}")
  public ResponseEntity<User> updateUserDetails(@PathVariable String userId, @RequestBody User user) {
    Utils.systemOutPrint("userId *****", userId);
    Utils.systemOutPrint("user *****", user);

    if (users.containsKey(userId)) {

      User existingUser = users.get(userId);

      /*if (user.getEmail() != null) {
        existingUser.setEmail(user.getEmail());
      }
      if (user.getPassword() != null) {
        existingUser.setPassword(user.getPassword());
      }
      if (user.getResetToken() != null) {
        existingUser.setResetToken(user.getResetToken());
      }

      users.put(userId, existingUser);*/

      userMapper.updateUserFromDto(user, existingUser);
      return ResponseEntity.ok(existingUser);
    }
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/delete-user/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    Utils.systemOutPrint("userId *****", userId);

    if (users.remove(userId) != null) {
      return ResponseEntity.ok().build();
    }
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
