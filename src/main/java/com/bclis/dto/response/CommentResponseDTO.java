package com.bclis.dto.response;

import com.bclis.persistence.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private int id;
    private String content;
    private LocalDateTime createdAt;
    private CommentEntity.CommentState commentState;
    private UserCommentResponseDTO user;
    private Long documentId;
}
