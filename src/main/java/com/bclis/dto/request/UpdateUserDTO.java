package com.bclis.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {

    private String name;
    private String lastName;
}
