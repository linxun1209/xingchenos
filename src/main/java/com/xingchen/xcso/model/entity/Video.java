package com.xingchen.xcso.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.xcso.model.entity
 * @date 2023/4/29 18:52
 */
@Data
public class Video implements Serializable {
    private String title;

    private String url;

    private String VUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

