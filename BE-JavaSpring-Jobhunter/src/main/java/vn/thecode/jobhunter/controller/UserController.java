package vn.thecode.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thecode.jobhunter.domain.User;
import vn.thecode.jobhunter.domain.response.ResCreateUserDTO;
import vn.thecode.jobhunter.domain.response.ResUpdateUserDTO;
import vn.thecode.jobhunter.domain.response.ResUserDTO;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.service.UserService;
import vn.thecode.jobhunter.util.annotation.ApiMessage;
import vn.thecode.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/${api.version}")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email" + user.getEmail() + "đã tồn tại, vui lòng thử lại");
        }
        String hasdPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hasdPassword);
        User newUser = userService.handleSaveUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.userService.convertToResCreateUserDTO(newUser));

    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) throws IdInvalidException {

        User currentUser = this.userService.handleFindUserById(id);
        if (currentUser == null)
            throw new IdInvalidException("User với ID " + id + " không tồn tại");

        userService.handleRemoveUserById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User user = userService.handleFindUserById(id);
        if (user != null) {
            ResUserDTO res = this.userService.convertToResUserDTO(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(res);
        } else {
            throw new IdInvalidException("User với ID " + id + " không tồn tại");
        }
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {

        ResultPaginationDTO users = userService.handleFindAllUsers(spec, pageable);
        ResponseEntity<ResultPaginationDTO> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
        return responseEntity;
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = userService.handleModifyUser(user);
        if (updatedUser == null) {
            throw new IdInvalidException("User với ID " + user.getId() + " không tồn tại");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.convertToResUpdateUserDTO(updatedUser));

    }
}
