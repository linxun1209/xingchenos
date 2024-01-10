package com.xingchen.xcso.model.vo;


import com.xingchen.xcso.model.entity.Picture;
import com.xingchen.xcso.model.entity.Video;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;


    private List<Video> videoList;


    private List<?> dataList;//只请求其中一个时可以用dataList


    private long total;

    private static final long serialVersionUID = 1L;

}
