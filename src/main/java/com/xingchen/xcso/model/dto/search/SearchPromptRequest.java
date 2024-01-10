package com.xingchen.xcso.model.dto.search;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author Reflux
 */

@Data
public class SearchPromptRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;
    private String type;

    private static final long serialVersionUID = 1L;
}