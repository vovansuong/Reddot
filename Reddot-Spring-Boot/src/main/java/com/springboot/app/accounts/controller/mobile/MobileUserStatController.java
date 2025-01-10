package com.springboot.app.accounts.controller.mobile;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.responce.MobileMemberResponse;
import com.springboot.app.accounts.dto.responce.MobileUserInfoResponse;
import com.springboot.app.accounts.service.MobileUserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/member")
public class MobileUserStatController {

    private static final Logger logger = LoggerFactory.getLogger(MobileUserStatController.class);

    @Autowired
    private MobileUserService mobileUserService;

    @GetMapping("/all")
    public ResponseEntity<List<MobileMemberResponse>> getAllMembers(
            @RequestParam(value = "search", defaultValue = "", required = false) String search
    ) {
        ServiceResponse<List<MobileMemberResponse>> response = mobileUserService.getMembersBy(search);
        if (response.getDataObject() != null && !response.getDataObject().isEmpty()) {
            return ResponseEntity.ok(response.getDataObject());
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{username}")
    public ResponseEntity<MobileUserInfoResponse> getMemberByUsername(@PathVariable String username) {
        ServiceResponse<MobileUserInfoResponse> response = mobileUserService.getMemberByUsername(username);
        if (response.getDataObject() != null) {
            return ResponseEntity.ok(response.getDataObject());
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/update")
    public ResponseEntity<MobileUserInfoResponse> updateMemberByUsername(@Valid @RequestBody AccountInfo userInfo) {
        ServiceResponse<MobileUserInfoResponse> response = mobileUserService.updateMemberByUsername(userInfo);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(response.getDataObject());
    }

}
