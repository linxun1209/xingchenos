package com.xingchen.xcso.model.entity;

import com.xingchen.xcso.model.vo.PostVO;
import com.xingchen.xcso.model.vo.UserVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 *
 * @author xing'chen
 * @from xingchen
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private static final long serialVersionUID = 1L;

}
