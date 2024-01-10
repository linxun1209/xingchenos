package com.xingchen.xcso.datasource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchen.xcso.common.ErrorCode;
import com.xingchen.xcso.exception.BusinessException;
import com.xingchen.xcso.model.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoDataSource implements DataSource<Video> {

    @Override
    public Page<Video> doSearch(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/videos/search?q=%s&first=%s",searchText,current);
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常! ");
        }
        Elements newsHeadlines = doc.select(".mc_vtvc_con_rc");
        List<Video> videos = new ArrayList<>();
        for (Element element : newsHeadlines) {//取视频地址
            //视频封面
            String ourl = element.attr("vscm");
            Map<String, Object> ourlMap = JSONUtil.toBean(ourl, Map.class);
            String videoCover = (String) ourlMap.get("turl");

            //获取 div标签
            Elements pic = element.select(".vrhdata");
            if (CollUtil.isEmpty(pic)) {
                log.error("bing video-html .vrhdata element is null");
                continue;
            }
            //vrhm 视频数据
            String vrhm = pic.attr("vrhm");
            if (StringUtils.isBlank(vrhm)) {
                log.error("bing video-html vrhm property is null");
                continue;
            }
            Map<String, Object> map = JSONUtil.toBean(vrhm, Map.class);
            //视频url
            String videoUrl = (String) map.get("murl");
            //视频标题
            String videoTitle = (String) map.get("vt");
            Video video = new Video();

            video.setTitle(videoTitle);
            video.setUrl(videoCover);
            video.setVUrl(videoUrl);
            // 检查视频URL是否可访问
            //if(isUrlAccessible(videoUrl))
            videos.add(video);
            if (videos.size() >= pageSize) {
                break;
            }
        }
        // 过滤URL为空的数据
        List<Video> collect = videos.stream()
                .filter(picture -> StringUtils.isNotBlank(picture.getTitle()) || StringUtils.isNotBlank(picture.getUrl()))
                .collect(Collectors.toList());
        Page<Video> videoPage = new Page<>(pageNum, pageSize);
        videoPage.setRecords(collect);
        return videoPage;
    }



    @Override
    public List<String> getSearchPrompt(String keyword) {
        return Collections.emptyList();
    }
}
