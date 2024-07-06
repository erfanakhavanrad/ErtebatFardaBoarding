package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Attachment;
import com.example.ertebatfardaboarding.domain.dto.AttachmentDto;
import com.example.ertebatfardaboarding.domain.responseDto.AttachmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentMapper attachmentMapper = Mappers.getMapper(AttachmentMapper.class);

    AttachmentDto attachmentToAttachmentDto(Attachment attachment);

    Attachment attachmentDtoToAttachment(AttachmentDto attachmentDto);

    AttachmentResponseDto attachmentToAttachmentResponseDto(Attachment attachment);

    AttachmentResponseDto attachmentDtoToAttachmentResponseDto(AttachmentDto attachmentDto);
}
