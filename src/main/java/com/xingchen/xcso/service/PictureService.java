package com.xingchen.xcso.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchen.xcso.model.entity.Picture;

/**
 * 图片服务
 *
 * @author xing'chen
 * @from xingchen
 */
public interface PictureService {

    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
