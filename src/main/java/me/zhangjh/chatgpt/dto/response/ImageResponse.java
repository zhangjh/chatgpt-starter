package me.zhangjh.chatgpt.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangjh
 * @date 3:13 PM 2022/12/15
 * @Description
 */
@Data
public class ImageResponse {

    private Date create;

    private List<ImageRet> data;

    private String errorMsg;
}
