package com.springboot.app.admin.controller;

import com.springboot.app.admin.dto.DashBoardResponse;
import com.springboot.app.admin.dto.DataForumGroupResponse;
import com.springboot.app.admin.service.AdminDashboardService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);


    @Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping("/total-data")
    public ResponseEntity<ObjectResponse> getDashboardData() {
        ServiceResponse<DashBoardResponse> response = adminDashboardService.getDashboardData();
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Success", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Not found", null));
    }

    @GetMapping("/chart-by-forum")
    public ResponseEntity<ObjectResponse> getCountUsersByForum() {
        ServiceResponse<List<DataForumGroupResponse>> response = adminDashboardService.getDataByForumGroup();
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Success", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Not found", null));
    }

}
