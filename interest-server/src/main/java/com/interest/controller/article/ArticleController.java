package com.interest.controller.article;

import com.interest.model.entity.PageResult;
import com.interest.model.request.ArticleCreateRequest;
import com.interest.model.request.ArticleUpdateRequest;
import com.interest.model.response.ArticleDetailResponse;
import com.interest.model.utils.PageWrapper;
import com.interest.model.utils.ResponseWrapper;
import com.interest.service.ArticleService;
import com.interest.service.InterestService;
import com.interest.utils.DateUtil;
import com.interest.utils.SecurityAuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private InterestService interestService;

    @PostMapping("/article")
    public ResponseWrapper<String> createArticle(@RequestBody ArticleCreateRequest articleCreateRequest) {
        articleService.createArticle(articleCreateRequest);
        return new ResponseWrapper<>("发布成功");
    }

    @PutMapping("/article")
    public ResponseWrapper<String> updateArticle(@RequestBody ArticleUpdateRequest articleCreateRequest) {
        articleService.updateArticle(articleCreateRequest);
        return new ResponseWrapper<>("发布成功");
    }

    @GetMapping("/public/articles")
    public ResponseWrapper<PageResult> getArticle(@RequestParam(value = "searchContent", required = false) String searchContent,
                                                  @RequestParam("pageSize") int pageSize,
                                                  @RequestParam("page") int page) {
        PageWrapper pageWrapper = new PageWrapper(pageSize, page);
        PageResult pageResult = articleService.getArticle(searchContent, pageWrapper);
        return new ResponseWrapper<>(pageResult);
    }

    @GetMapping("/admin/articles")
    public ResponseWrapper<PageResult> getArticles(@RequestParam(value = "searchContent", required = false) String searchContent,
                                                   @RequestParam(value = "dateTimestamp", required = false) String dateTimestamp,
                                                   @RequestParam(value = "del", required = false) int del,
                                                   @RequestParam("pageSize") int pageSize,
                                                   @RequestParam("page") int page) {
        PageWrapper pageWrapper = new PageWrapper(pageSize, page);
        PageResult pageResult = articleService.getArticleOnManagement(searchContent, dateTimestamp, del, pageWrapper);
        return new ResponseWrapper<>(pageResult);
    }

    @DeleteMapping("/admin/articles")
    public ResponseWrapper delArticles(@RequestBody List<String> groupId) {
        articleService.updateArticlesDelByIds(groupId, 1);
        return new ResponseWrapper<>(groupId);
    }

    @DeleteMapping("/articles/article/{id}")
    public ResponseWrapper delArticleById(@PathVariable("id") int articleId) {
        articleService.updateArticlesDelById(articleId);
        return new ResponseWrapper<>(articleId);
    }

    @PatchMapping("/admin/articles")
    public ResponseWrapper republishArticles(@RequestBody List<String> groupId) {
        articleService.updateArticlesDelByIds(groupId, 0);
        return new ResponseWrapper<>(groupId);
    }

    @PatchMapping("/admin/articles/top")
    public ResponseWrapper changeArticlesTop(@RequestBody List<String> groupId, @RequestParam("top") int top) {
        articleService.updateArticlesTopByIds(groupId, top);
        return new ResponseWrapper<>(groupId);
    }

    @GetMapping("/public/users/user/{userId}/articles")
    public ResponseWrapper<PageResult> getArticle(@PathVariable("userId") int userId,
                                                  @RequestParam("pageSize") int pageSize,
                                                  @RequestParam("page") int page) {
        PageWrapper pageWrapper = new PageWrapper(pageSize, page);
        PageResult pageResult = articleService.getArticlesByUserId(userId, pageWrapper);
        return new ResponseWrapper<>(pageResult);
    }

    @GetMapping("/general/users/user/articles")
    public ResponseWrapper<PageResult> getUserArticle(@RequestParam("pageSize") int pageSize,
                                                  @RequestParam("page") int page) {
        int userId = SecurityAuthenUtil.getId();
        PageWrapper pageWrapper = new PageWrapper(pageSize, page);
        PageResult pageResult = articleService.getArticlesByUserId(userId, pageWrapper);
        return new ResponseWrapper<>(pageResult);
    }

    @GetMapping("/public/articles/article/{id}")
    public ResponseWrapper<ArticleDetailResponse> getArticleById(@PathVariable("id") int id) {
        ArticleDetailResponse articleDetailResponse = articleService.getArticleById(id);
        return new ResponseWrapper<>(articleDetailResponse);
    }

    @PostMapping("/upload/picture")
    public ResponseWrapper<String> uploadPicture(@RequestParam("picture") MultipartFile picture) {

        String pictureUrl = interestService.savePicture(picture);
        return new ResponseWrapper<>(pictureUrl);
    }

}
