package com.wechat.ferry.entity.vo.response;


import lombok.Data;

import java.util.List;

/**
 * 请求出参-个微WCF数据库记录
 *
 * @author chandler
 * @date 2024/10/02 17:14
 */
@Data
public class WxPpWcfDatabaseRowResp {

    /**
     * 字段列表
     */
    private List<WxPpWcfDatabaseFieldResp> fieldList;

}
