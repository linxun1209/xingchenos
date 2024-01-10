package com.xingchen.xcso.controller;


import com.xingchen.xcso.common.BaseResponse;
import com.xingchen.xcso.common.ResultUtils;
import com.xingchen.xcso.model.dto.post.PostEsDTO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索建议
 * @author lacy
 */
@RestController
@RequestMapping("/suggest")
public class SuggestController {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @GetMapping("/post")
    public BaseResponse<List<String>> searchAll(String pre) {
        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.completionSuggestion("title.suggest").prefix(pre).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("my-suggest", termSuggestionBuilder);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.fetchSource(new String[] {"title"}, null);
        SearchResponse suggest = elasticsearchRestTemplate.suggest(suggestBuilder, PostEsDTO.class);
        Suggest.Suggestion suggestion = suggest.getSuggest().getSuggestion("my-suggest");
        List<String> returnList = new ArrayList<>();
        List<Suggest.Suggestion.Entry> list = suggestion.getEntries();
        for(Suggest.Suggestion.Entry entry : list) {
            List<Suggest.Suggestion.Entry.Option> options = entry.getOptions();
            for(Suggest.Suggestion.Entry.Option option : options) {
                String op = option.getText().toString();
                if (!returnList.contains(op)){
                    returnList.add(option.getText().toString());
                }
            }
        }
        return ResultUtils.success(returnList);
    }
}
