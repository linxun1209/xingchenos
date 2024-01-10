package com.xingchen.xcso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchen.xcso.common.BaseResponse;
import com.xingchen.xcso.common.ErrorCode;
import com.xingchen.xcso.common.ResultUtils;
import com.xingchen.xcso.exception.ThrowUtils;
import com.xingchen.xcso.model.dto.picture.PictureQueryRequest;
import com.xingchen.xcso.model.entity.Picture;
import com.xingchen.xcso.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author xing'chen
 * @from xingchen
 */
@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }


}
