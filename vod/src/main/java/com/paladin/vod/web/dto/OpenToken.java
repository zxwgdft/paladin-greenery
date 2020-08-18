package com.paladin.vod.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/3/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "token")
public class OpenToken {

    @ApiModelProperty("token")
    private String accessToken;
    @ApiModelProperty("过期时长")
    private long expiresTime;

}
