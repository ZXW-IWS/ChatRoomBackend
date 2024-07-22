package com.zuu.chatroom.common.domain.vo.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 20:39
 */
@Data
@Schema(title = "基础翻页请求")
public class PageBaseReq {

    @Schema(title = "页面大小")
    @Min(0)
    @Max(50)
    private Integer pageSize = 10;

    @Schema(title = "页面索引（从1开始）")
    private Integer pageNo = 1;

}

