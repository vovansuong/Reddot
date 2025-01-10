package com.springboot.app.admin.service;

import com.springboot.app.admin.dto.DashBoardResponse;
import com.springboot.app.admin.dto.DataForumGroupResponse;
import com.springboot.app.dto.response.ServiceResponse;

import java.util.List;

public interface AdminDashboardService {
    ServiceResponse<DashBoardResponse> getDashboardData();

    ServiceResponse<List<DataForumGroupResponse>> getDataByForumGroup();

}
