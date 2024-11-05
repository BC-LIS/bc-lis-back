package com.bclis.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDTO {
    private String content;
    private Long documentId;
}
