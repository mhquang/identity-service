package com.identity_service.controller;

import com.identity_service.dto.request.PermissionRequest;
import com.identity_service.dto.request.RoleRequest;
import com.identity_service.dto.response.APIResponse;
import com.identity_service.dto.response.PermissionResponse;
import com.identity_service.dto.response.RoleResponse;
import com.identity_service.service.PermissionService;
import com.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public APIResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public APIResponse<List<RoleResponse>> getAll() {
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    public APIResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return APIResponse.<Void>builder().build();
    }
}
