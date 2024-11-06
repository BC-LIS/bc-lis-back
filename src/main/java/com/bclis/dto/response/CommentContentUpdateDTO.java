package com.bclis.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentContentUpdateDTO {
    private Long id;
    private String content;
}
