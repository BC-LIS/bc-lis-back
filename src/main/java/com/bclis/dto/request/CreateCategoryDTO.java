package com.bclis.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryDTO {
    private String categoryName;
}
