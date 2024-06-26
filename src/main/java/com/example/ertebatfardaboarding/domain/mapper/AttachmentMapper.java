package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Attachment;
import com.example.ertebatfardaboarding.domain.AttachmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttachmentMapper {
    AttachmentMapper attachmentMapper = Mappers.getMapper(AttachmentMapper.class);

    AttachmentDto attachmentToAttachmentDto(Attachment attachment);

    Attachment attachmentDtoToAttachment(AttachmentDto attachmentDto);
}
