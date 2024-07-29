package com.zuu.chatroom.common.domain.vo.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 10:51
 */
@Data
@Schema(title = "游标翻页请求")
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseReq {

    @Schema(title = "页面大小")
    @Min(0)
    @Max(100)
    private Integer pageSize = 10;

    @Schema(title = "游标（初始为null，后续请求附带上次翻页的游标）")
    private String cursor;
}
