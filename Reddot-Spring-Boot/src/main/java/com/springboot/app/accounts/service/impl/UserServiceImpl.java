package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.NewPasswordRequest;
import com.springboot.app.accounts.dto.request.UpdateRoleRequest;
import com.springboot.app.accounts.entity.*;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.enumeration.AuthProvider;
import com.springboot.app.accounts.enumeration.RoleName;
import com.springboot.app.accounts.repository.DeletedUserRepository;
import com.springboot.app.accounts.repository.RecoveryTokenRepository;
import com.springboot.app.accounts.repository.RoleRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import com.springboot.app.email.repository.EmailContentOptionRepository;
import com.springboot.app.email.service.EmailOptionService;
import com.springboot.app.follows.repository.FollowUserRepository;
import com.springboot.app.security.dto.request.SignupRequest;
import com.springboot.app.security.service.RefreshTokenService;
import com.springboot.app.service.email.MailSenderManager;
import com.springboot.app.utils.ResourceUtil;
import com.springboot.app.utils.Validators;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;
    @Autowired
    private DeletedUserRepository deletedUserRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private FollowUserRepository followUserRepository;

    @Autowired
    private MailSenderManager mailSenderManager;
    @Autowired
    private EmailContentOptionRepository emailContentOptionRepository;
    @Autowired
    private EmailOptionService emailOptionService;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ServiceResponse<List<User>> findUserByRoleMod() {
        ServiceResponse<List<User>> response = new ServiceResponse<>();
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals(RoleName.ROLE_MODERATOR))).toList();
        if (users != null) {
            response.setDataObject(users);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User not found");
        }
        return response;
    }

    @Override
    public PaginateResponse getAllUsers(int pageNo, int pageSize, String orderBy, String sortDir, String keyword) {
        logger.info("Fetching all users from the database");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<User> usersPage = userRepository.searchByUsernameOrEmail(keyword, keyword, pageable);

        return new PaginateResponse(usersPage.getNumber() + 1, usersPage.getSize(), usersPage.getTotalPages(),
                usersPage.getContent().size(), usersPage.isLast(), usersPage.getContent());
    }

    @Override
    public ServiceResponse<User> createNewUser(SignupRequest request) {
        ServiceResponse<User> response = new ServiceResponse<>();
        try {
            List<String> errorMessages = validateUser(request);
            if (!errorMessages.isEmpty()) {
                String err = "Error: User not created. %s".formatted(errorMessages);
                logger.error(err);
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessages(errorMessages);
                return response;
            }
            // Create new user's account
            User user = new User(request.getUsername(), request.getEmail(),
                    encoder.encode(request.getPassword()),
                    AuthProvider.local);

            Set<String> strRoles = request.getRoles();
            Set<Role> roles = getRolesByString(strRoles);
            user.setRoles(roles);
            user.setCreatedBy(user.getUsername());

            Person person = new Person();
            user.setPerson(person);

            UserStat userStat = new UserStat();
            userStat.setCreatedBy(user.getUsername());
            user.setStat(userStat);

            userRepository.save(user);
            EmailContentOption contentOption = emailOptionService.getEmailOptionByType(EmailOptionType.REGISTRATION_CONFIRMATION);
            ResourceUtil.ResourcePartObject partObject = new ResourceUtil.ResourcePartObject(user.getUsername(), user.getEmail(), null, null);
            String msg = ResourceUtil.buildEmailContentFromTemplate(contentOption.getEmailBodyTemplate(), partObject);
            mailSenderManager.sendEmail(request.getEmail(), "Reddot account confirmation", msg, true);
            response.setDataObject(user);

        } catch (Exception e) {
            logger.error("Error: User not created. %s".formatted(e.getMessage()));
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public boolean createOAuthUser(User user) {
        try {
            // generate 10 character uuid
            String plainPw = "@Pss" + UUID.randomUUID().toString().substring(0, 10);
            user.setPassword(createEncodedPassword(plainPw));
            Person person = new Person();
            person.setCreatedBy(user.getUsername());
            user.setPerson(person);
            UserStat userStat = new UserStat();
            userStat.setCreatedBy(user.getUsername());
            user.setStat(userStat);
            roleRepository.findByName(RoleName.ROLE_USER).ifPresent(role -> user.setRoles(new HashSet<>(Set.of(role))));
            userRepository.save(user);

            EmailContentOption contentOption = emailContentOptionRepository.findByOptionType(EmailOptionType.REGISTRATION_WITH_OAUTH_NOTIFICATION);
            ResourceUtil.ResourcePartObject partObject = new ResourceUtil.ResourcePartObject(user.getUsername(), user.getEmail(), plainPw, null);
            String msg = ResourceUtil.buildEmailContentFromTemplate(contentOption.getEmailBodyTemplate(), partObject);
            mailSenderManager.sendEmail(user.getEmail(), contentOption.getEmailSubject(), msg, true);
            return true;
        } catch (Exception e) {
            logger.error("Error occurred while creating OAuth user: %s".formatted(e.getMessage()));
            return false;
        }
    }

    private @NotBlank @Size(min = 6, max = 100) String createEncodedPassword(String plainPw) {
        return encoder.encode(plainPw);
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Void> deleteUser(User user) {

        ServiceResponse<Void> response = new ServiceResponse<>();
        DeletedUser deletedUser = DeletedUser.fromUser(user);

        // clear up relationships before deleting
        user.setPerson(null);
        user.setStat(null);
        // delete refresh tokens
        refreshTokenService.deleteByUserId(user.getId());
        // delete followed users
        followUserRepository.deleteBy(user.getId());

        // delete user
        userRepository.delete(user);

        // save deletedUser
        deletedUserRepository.save(deletedUser);

        return response;
    }

    @Override
    @Transactional()
    public ServiceResponse<User> updateStatusUser(Long id, String status) {
        ServiceResponse<User> response = new ServiceResponse<>();
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            AccountStatus accountStatus = getAccountStatus(status);
            user.setAccountStatus(accountStatus);
            userRepository.save(user);
            response.setDataObject(user);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User not found");
        }
        return response;
    }

    @Override
    public void updateLastLogin(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getStat().setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    @Transactional()
    public ServiceResponse<User> updateRoleUser(UpdateRoleRequest updateRoleRequest) {
        ServiceResponse<User> response = new ServiceResponse<>();
        User user = userRepository.findById(updateRoleRequest.getUserId()).orElse(null);
        if (user != null) {
            Set<Role> roles = getRolesByString(updateRoleRequest.getRoles());
            user.setRoles(roles);
            userRepository.save(user);
            response.setDataObject(user);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User not found");
        }
        return response;
    }

    private AccountStatus getAccountStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> AccountStatus.ACTIVE;
            case "LOCKED" -> AccountStatus.LOCKED;
            default -> AccountStatus.INACTIVE;
        };
    }

    private Set<Role> getRolesByString(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(findRoleByName(RoleName.ROLE_USER));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(findRoleByName(RoleName.ROLE_ADMIN));
                        break;
                    case "mod":
                        roles.add(findRoleByName(RoleName.ROLE_MODERATOR));
                        break;
                    default:
                        roles.add(findRoleByName(RoleName.ROLE_USER));
                        break;
                }
            });
        }
        return roles;
    }

    private Role findRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    private List<String> validateUser(SignupRequest user) {

        List<String> messages = new ArrayList<>();
        if (!Validators.isUsernameValid(user.getUsername())) {
            messages.add("Invalid Username Format");
        } else if (userRepository.existsByUsername(user.getUsername())
                   || deletedUserRepository.existsByUsername(user.getUsername())) {
            messages.add("Username already exists in the system");
        }

        if (!Validators.isEmailValid(user.getEmail())) {
            messages.add("Invalid Email Format");
        } else if (userRepository.existsByEmail(user.getEmail())
                   || deletedUserRepository.existsByEmail(user.getEmail())) {
            messages.add("Email already exists in the system");
        }

        if (!Validators.isPasswordValid(user.getPassword())) {
            messages.add("Invalid Password Format");
        }

        return messages;
    }

    @Transactional()
    public ServiceResponse<Void> passwordReset(RecoveryToken recoveryToken, String newPassword) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        User user = userRepository.findByEmail(recoveryToken.getEmail()).orElse(null);
        if (user != null && !"".equals(newPassword)) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            recoveryTokenRepository.delete(recoveryToken);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage(String.format("Unable to locate user with email %s", recoveryToken.getEmail()));
        }
        return response;
    }

    @Transactional(readOnly = false)
    public ServiceResponse<Void> updatePasswordReset(String newPassword, User user) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        if ("".equals(newPassword)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("New Password must not be empty");
        } else {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        }
        return response;
    }

    // Update user password with new password but verify the oldPassword with
    // current user.password
    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Void> updateNewPassword(NewPasswordRequest newPasswordRequest, User user) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        if (!encoder.matches(newPasswordRequest.getOldPassword(), user.getPassword())) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Current password is incorrect");
            return response;
        }
        if (!Validators.isPasswordValid(newPasswordRequest.getNewPassword())) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Invalid Password Format");
            return response;
        }

        if (newPasswordRequest.getOldPassword().equals(newPasswordRequest.getNewPassword())) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("New Password must be different from the old password");
            return response;
        }

        user.setPassword(encoder.encode(newPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return response;
    }

}
