package com.bclis.dto.response;

import com.bclis.persistence.entity.enums.EnumRole;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponseDTO {

    private EnumRole roleName;
}
