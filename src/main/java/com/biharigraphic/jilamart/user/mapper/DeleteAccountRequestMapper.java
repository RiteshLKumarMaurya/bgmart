package com.biharigraphic.jilamart.user.mapper;

import com.biharigraphic.jilamart.user.dto.request.DeleteAccountRequestDto;
import com.biharigraphic.jilamart.user.entity.DeleteAccountRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeleteAccountRequestMapper {

    //to entity
    public DeleteAccountRequest toEntity(DeleteAccountRequestDto dto);

}

