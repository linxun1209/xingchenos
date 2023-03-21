package com.xingchen.xcso.controller;

import com.xingchen.xcso.manager.SearchFacade;
import com.xingchen.xcso.model.dto.search.SearchRequest;
import com.xingchen.xcso.common.BaseResponse;
import com.xingchen.xcso.common.ResultUtils;
import com.xingchen.xcso.model.vo.SearchVO;
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
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }
}
