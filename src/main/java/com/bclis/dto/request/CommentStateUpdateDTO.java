package com.bclis.dto.request;

import com.bclis.persistence.entity.CommentEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentStateUpdateDTO {
    private Long id;
    private CommentEntity.CommentState commentState;
}
