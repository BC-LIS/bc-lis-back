package com.bclis.dto.request;

import com.bclis.persistence.entity.enums.EnumRole;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFiltersDto {

    private String name;
    private EnumRole role;
    private Boolean isActive;
}
