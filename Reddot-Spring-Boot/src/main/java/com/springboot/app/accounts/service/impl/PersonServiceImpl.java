package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.responce.AccountInfoResponse;
import com.springboot.app.accounts.entity.Person;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.enumeration.Gender;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.PersonService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public ServiceResponse<Void> updatePersonalInfo(User user, AccountInfo accountInfo) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        List<String> errors = validatePersonForUpdate(accountInfo);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }
        Person person = user.getPerson();
        person.setAddress(accountInfo.getAddress());
        person.setPhone(accountInfo.getPhone());
        person.setBirthDate(accountInfo.getBirthday());
        person.setGender(convertGender(accountInfo.getGender()));
        person.setBio(accountInfo.getBio());

        user.setPerson(person);
        user.setName(accountInfo.getName());
        user.setEmail(accountInfo.getEmail());

        userRepository.save(user);
        response.addMessage("Update personal info successfully");
        return response;
    }

    private Gender convertGender(String gender) {
        if ("MALE".equals(gender)) {
            return Gender.MALE;
        }
        if ("FEMALE".equals(gender)) {
            return Gender.FEMALE;
        }
        return Gender.OTHER;
    }


    private List<String> validatePersonForUpdate(AccountInfo accountInfo) {
        List<String> errors = new ArrayList<>();
        if (accountInfo == null) {
            errors.add("Person is not found");
            return errors;
        }
        if ("".equals(accountInfo.getName())) {
            errors.add("Full Name must not be empty");
        }
        if ("".equals(accountInfo.getPhone())) {
            errors.add("Phone number must not be empty");
        }
        if ("".equals(accountInfo.getAddress())) {
            errors.add("Address must not be empty");
        }
        //check if birthday must be over 18 years old
        if (accountInfo.getBirthday() != null) {
            LocalDate now = LocalDate.now();
            int age = now.getYear() - accountInfo.getBirthday().getYear();
            if (age < 18) {
                errors.add("Birthday must be over 18 years old");
            }
        }
        return errors;
    }

    public ServiceResponse<String> getAvatarByUsername(String username) {
        ServiceResponse<String> response = new ServiceResponse<>();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User not found");
            return response;
        }
        var img = user.getImageUrl() == null ? user.getAvatar() : user.getImageUrl();
        response.setDataObject(img);
        return response;
    }

    //get user info, not user deleted
    public ServiceResponse<AccountInfoResponse> getUserInfoByUsername(String username) {
        ServiceResponse<AccountInfoResponse> response = new ServiceResponse<>();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User not found");
            return response;
        }
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setId(user.getId());
        accountInfoResponse.setUsername(user.getUsername());
        accountInfoResponse.setEmail(user.getEmail());
        accountInfoResponse.setName(user.getName());
        accountInfoResponse.setPerson(user.getPerson());
        accountInfoResponse.setUserStat(user.getStat());
        accountInfoResponse.setAvatar(user.getAvatar());
        accountInfoResponse.setImageUrl(user.getImageUrl());

        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        accountInfoResponse.setRoles(roles);

        response.setDataObject(accountInfoResponse);
        return response;
    }


}
