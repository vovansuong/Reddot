package com.springboot.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginateResponse {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
    private boolean last;
    private Object data;

}
