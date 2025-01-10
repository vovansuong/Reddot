package com.springboot.app.bannedKeyword;

import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banned-keywords")
public class BannedKeywordController {
    @Autowired
    private BannedKeyWordService bannedKeywordService;
    @Autowired
    private BannedKeywordRepository bannedKeywordRepository;

    @GetMapping("/all")
    public ResponseEntity<ObjectResponse> getAllBannedKeywords() {
        List<BannedKeyword> bannedKeywords = bannedKeywordService.getAllBannedKeywords().getDataObject();
        return ResponseEntity.ok(new ObjectResponse("200", "Success", bannedKeywords));
    }

    @GetMapping("/paginate")
    public ResponseEntity<ObjectResponse> getAllBannedKeyword(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity
                .ok(new ObjectResponse("201", "Success", bannedKeywordService.getPageBannedKeyword(page, size, orderBy, sort, search)));
    }


    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> saveBannedKeyword(@RequestBody BannedKeyword bannedKeyword) {
        String username = null;
        try {
            var userSession = JwtUtils.getSession();
            username = userSession.getUsername();
        } catch (Exception e) {
            return ResponseEntity.ok(new ObjectResponse("401", "Unauthorized", null));
        }
        bannedKeyword.setCreatedBy(username);
        BannedKeyword savedBannedKeyword = bannedKeywordService.saveBannedKeyword(bannedKeyword).getDataObject();
        return ResponseEntity.ok(new ObjectResponse("200", "Success", savedBannedKeyword));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> updateBannedKeyword(@PathVariable Long id, @RequestBody BannedKeyword bannedKeyword) {
        BannedKeyword oldBannedKeyword = bannedKeywordRepository.findById(id).orElse(null);
        if (oldBannedKeyword == null) {
            return ResponseEntity.ok(new ObjectResponse("404", "Not Found", null));
        }
        String username = null;
        try {
            var userSession = JwtUtils.getSession();
            username = userSession.getUsername();
        } catch (Exception e) {
            return ResponseEntity.ok(new ObjectResponse("401", "Unauthorized", null));
        }
        oldBannedKeyword.setUpdatedBy(username);
        oldBannedKeyword.setKeyword(bannedKeyword.getKeyword());
        BannedKeyword updatedBannedKeyword = bannedKeywordService.saveBannedKeyword(oldBannedKeyword).getDataObject();
        return ResponseEntity.ok(new ObjectResponse("200", "Success", updatedBannedKeyword));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> deleteBannedKeyword(@PathVariable Long id) {
        BannedKeyword deletedBannedKeyword = bannedKeywordService.deleteBannedKeyword(id).getDataObject();
        return ResponseEntity.ok(new ObjectResponse("200", "Success", deletedBannedKeyword));
    }

}
