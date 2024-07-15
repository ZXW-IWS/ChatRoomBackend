package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:46
 */
@Data
public class ModifyNameReq {
    @Schema(title = "修改的用户名")
    @NotBlank
    @Length(max = 6,message = "用户名不可以取太长")
    private String nickname;
    @Schema(title = "用户id")
    @NotNull
    private Integer id;
}
