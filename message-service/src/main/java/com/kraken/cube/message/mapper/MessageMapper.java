package com.kraken.cube.message.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;

import com.kraken.cube.message.MessageApplication;
import com.kraken.cube.message.dto.CreateMessageRequest;
import com.kraken.cube.message.dto.MessageDto;
import com.kraken.cube.message.dto.MessageResponseDto;
import com.kraken.cube.message.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto entityToDto(Message entity);

    List<MessageDto> listEntityToListDto(List<Message> list);

    @Mapping(target = "id", ignore = true)
    Message dtoToEntity(MessageDto dto);
    
    
    
    Message createRequestDtoToEntity(CreateMessageRequest dto);
    @Mapping(target = "messageId", source = "id")
    MessageResponseDto entityToMessageResponseDto(Message entity);
    List<MessageResponseDto> entityListToMessageResponseDtoList(List<Message> entity);

}
