package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.dto.request.UpdateRoleRequest;
import com.springboot.app.accounts.dto.request.UpdateStatusRequest;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.utils.Validators;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/moderators")
    public ResponseEntity<ObjectResponse> getModerators() {
        ServiceResponse<List<User>> response = userService.findUserByRoleMod();
        if (response.getAckCode().equals(AckCodeType.SUCCESS)) {
            return ResponseEntity.ok(new ObjectResponse("200", "Success moderators", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("500", "Failed to get moderators", null));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginateResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, orderBy, sort, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectResponse> getUserById(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(new ObjectResponse("404", "User not found", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Success user", user));
    }

    @GetMapping("/account/{username}")
    public ResponseEntity<ObjectResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ObjectResponse("404", "User not found", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Success user", user));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ObjectResponse("404", "User not found", null));
        }
        // check user has role admin
        if (user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            return ResponseEntity.badRequest()
                    .body(new ObjectResponse("400", "Cannot delete user with role admin", null));
        }
        ServiceResponse<Void> response = userService.deleteUser(user);
        if (response.getAckCode().equals(AckCodeType.SUCCESS)) {
            return ResponseEntity.ok(new ObjectResponse("200", "User deleted", null));
        }
        return ResponseEntity.ok(new ObjectResponse("500", "Failed to delete user", null));
    }

    @PostMapping("status/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updateStatusUser(@PathVariable Long id,
                                                           @RequestBody UpdateStatusRequest updateStatusRequest) {
        if (!Objects.equals(id, updateStatusRequest.getUserId())) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Invalid user id", null));
        }
        String status = updateStatusRequest.getStatus();
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("LOCKED")) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Invalid status", null));
        }
        ServiceResponse<User> response = userService.updateStatusUser(id, status);
        if (response.getAckCode().equals(AckCodeType.SUCCESS)) {
            return ResponseEntity.ok(new ObjectResponse("200", "User status updated", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("500", "Failed to update user status", null));
    }

    @PostMapping("role/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updateRoleUser(@Valid @RequestBody UpdateRoleRequest updateRoleRequest) {
        var session = JwtUtils.getSession();
        if (updateRoleRequest.getUserId().equals(session.getId())) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Cannot update your own role", null));
        }
        // check user has role admin
        boolean isRoleValid = updateRoleRequest.getRoles().stream().allMatch(Validators::isValidRole);
        if (!isRoleValid) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Invalid role", null));
        }
        ServiceResponse<User> response = userService.updateRoleUser(updateRoleRequest);
        if (response.getAckCode().equals(AckCodeType.SUCCESS)) {
            return ResponseEntity.ok(new ObjectResponse("200", "User role updated", response.getDataObject()));
        }
        return ResponseEntity.badRequest().body(new ObjectResponse("400", "Failed to update user role", null));
    }

}
