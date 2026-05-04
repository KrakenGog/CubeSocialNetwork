package com.kraken.cube.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChatPermissionDto {
    private boolean canWrite;
    private String errorCode;
}
