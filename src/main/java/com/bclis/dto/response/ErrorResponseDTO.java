package com.bclis.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorResponseDTO {
    private String code;
    private String message;
}
