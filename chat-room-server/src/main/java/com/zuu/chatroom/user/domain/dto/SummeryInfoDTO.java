package com.zuu.chatroom.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//NULL值不必序列化
public class SummeryInfoDTO {
    @Schema(title =  "用户id")
    private Long uid;
    @Schema(title =  "是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;
    @Schema(title =  "用户昵称")
    private String name;
    @Schema(title =  "用户头像")
    private String avatar;
    @Schema(title =  "归属地")
    private String locPlace;
    @Schema(title = "佩戴的徽章id")
    private Long wearingItemId;
    @Schema(title =  "用户拥有的徽章id列表")
    List<Long> itemIds;

}