package com.xingchen.xcso.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.xingchen.xcso.common.ErrorCode;
import com.xingchen.xcso.datasource.*;
import com.xingchen.xcso.exception.BusinessException;
import com.xingchen.xcso.exception.ThrowUtils;
import com.xingchen.xcso.model.dto.post.PostQueryRequest;
import com.xingchen.xcso.model.dto.search.SearchPromptRequest;
import com.xingchen.xcso.model.dto.search.SearchRequest;
import com.xingchen.xcso.model.dto.user.UserQueryRequest;
import com.xingchen.xcso.model.entity.Picture;
import com.xingchen.xcso.model.entity.Video;
import com.xingchen.xcso.model.enums.SearchTypeEnum;
import com.xingchen.xcso.model.vo.PostVO;
import com.xingchen.xcso.model.vo.SearchVO;
import com.xingchen.xcso.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private VideoDataSource videoDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(SearchRequest searchAllRequest) {

        String type = searchAllRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        //ThrowUtils.throwIf(type == null, ErrorCode.PARAMS_ERROR);
        //不是三种枚举类型，默认全查
        String searchText = searchAllRequest.getSearchText();
        long pageNum = searchAllRequest.getCurrent();
        long pageSize = searchAllRequest.getPageSize();
        if (searchTypeEnum.getValue() == null) {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            RequestContextHolder.setRequestAttributes(sra, true);

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> pictureDataSource.doSearch(searchText, pageNum, pageSize));

            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> userDataSource.doSearch(searchText, pageNum, pageSize));

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> postDataSource.doSearch(searchText, pageNum, pageSize));
            CompletableFuture<Page<Video>> videoTask = CompletableFuture.supplyAsync(() -> videoDataSource.doSearch(searchText, pageNum, pageSize));

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();//等上面三个异步全完成，短板效应，看谁最慢

            try {
                Page<Picture> picturePage = pictureTask.get();
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Video> videoPage = videoTask.get();
                SearchVO searchAllVO = new SearchVO();
                searchAllVO.setUserList(userVOPage.getRecords());
                searchAllVO.setPostList(postVOPage.getRecords());
                searchAllVO.setPictureList(picturePage.getRecords());
                searchAllVO.setVideoList(videoPage.getRecords());
                searchAllVO.setTotal(postVOPage.getTotal());//设置帖子总数
                return searchAllVO;
            } catch (Exception e) {
                log.error("searchAll查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "searchAll查询异常");
            }
        } else {
            SearchVO searchAllVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, pageNum, pageSize);
            searchAllVO.setDataList(page.getRecords());
            searchAllVO.setTotal(page.getTotal());//设置Post或User的total
            return searchAllVO;
        }
    }


    public List<String> getSearchPrompt(SearchPromptRequest searchPromptRequest) {
        String type = searchPromptRequest.getType();//类型
        String keyword = searchPromptRequest.getSearchText();//搜索关键词
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        if (searchTypeEnum != null) {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            // 搜索建议统一返回字符串，就不需要泛型了
            return dataSource.getSearchPrompt(keyword);

        } else return Collections.emptyList();//不在我们标签里的，就不用提供搜索建议服务
    }
}
