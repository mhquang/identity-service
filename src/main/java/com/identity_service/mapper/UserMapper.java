package com.identity_service.mapper;

import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.request.UserUpdateRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User updateUser, UserUpdateRequest request);

    UserResponse toUserResponse(User user);
}
