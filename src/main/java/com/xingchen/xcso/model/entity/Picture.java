package com.xingchen.xcso.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图片
 * @author xing'chen
 */
@Data
public class Picture implements Serializable {

    private String title;

    private String url;

    private List<Picture> pictureList;

    private static final long serialVersionUID = 1L;

}
