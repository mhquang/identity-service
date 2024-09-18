package com.identity_service.controller;

import com.identity_service.dto.request.PermissionRequest;
import com.identity_service.dto.response.APIResponse;
import com.identity_service.dto.response.PermissionResponse;
import com.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public APIResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    public APIResponse<List<PermissionResponse>> getAll() {
        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    public APIResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return APIResponse.<Void>builder().build();
    }
}
