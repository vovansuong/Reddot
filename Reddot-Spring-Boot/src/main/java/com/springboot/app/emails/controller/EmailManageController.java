package com.springboot.app.emails.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.dto.request.DataEmailRequest;
import com.springboot.app.emails.dto.request.PassResetEmailRequest;
import com.springboot.app.emails.dto.request.RegistationEmailRequest;
import com.springboot.app.emails.dto.response.DataEmailResponse;
import com.springboot.app.emails.entity.EmailOption;
import com.springboot.app.emails.entity.RegistrationOption;
import com.springboot.app.emails.service.EmailOptionsService;
import com.springboot.app.emails.service.RegistrationOptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/email-manage")
public class EmailManageController {
    private static final Logger logger = LoggerFactory.getLogger(EmailManageController.class);

    private final EmailOptionsService emailOptionService;

    private final RegistrationOptionService registrationOptionService;

    @Autowired
    public EmailManageController(EmailOptionsService emailOptionService, RegistrationOptionService registrationOptionService) {
        this.emailOptionService = emailOptionService;
        this.registrationOptionService = registrationOptionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> getEmailOptionById() {
        Long id = 1L;
        logger.info("Get email option by id {}", id);
        ServiceResponse<EmailOption> response = emailOptionService.getEmailOptionById(id);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Email option retrieved", response.getDataObject()));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> addEmailOption(@Valid @RequestBody EmailOption emailOption) {
        logger.info("Add email option");
        ServiceResponse<EmailOption> response = emailOptionService.saveEmailOption(emailOption);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("201", "Email option added", response.getDataObject()));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updateEmailOption(@Valid @RequestBody EmailOption emailOption) {
        Long id = 1L;
        logger.info("Update email option by id {}", id);
        ServiceResponse<Void> response = emailOptionService.updateEmailOption(id, emailOption);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Email option updated", null));
    }


    @GetMapping("/registration")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> getRegistrationOptionById() {
        Long id = 1L;
        logger.info("Get registration option by id {}", id);
        ServiceResponse<RegistrationOption> response = registrationOptionService.getRegistrationOptionById(id);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Registration option retrieved", response.getDataObject()));
    }

    @PostMapping("/registration/update-pass-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updatePassResetEmailOption(@Valid @RequestBody PassResetEmailRequest option) {
        ServiceResponse<Void> response = registrationOptionService.updatePassResetEmailOption(option);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Registration option updated", null));
    }

    @PostMapping("/registration/update-reg-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updateRegEmailOption(@Valid @RequestBody RegistationEmailRequest option) {
        ServiceResponse<Void> response = registrationOptionService.updateRegistrationEmailOption(option);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Registration option updated", null));
    }


    @PostMapping("/send-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> sendDataEmail(@Valid @RequestBody DataEmailRequest dataEmailRequest) {
        ServiceResponse<Void> response = emailOptionService.sendDataEmail(dataEmailRequest);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Test email sent", null));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> getAllEmailData() {
        ServiceResponse<List<DataEmailResponse>> response = emailOptionService.getAllEmail();
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "No data", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Email data retrieved", response.getDataObject()));
    }


}
