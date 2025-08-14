package bh.board.article.service;

import bh.board.article.dto.ArticleCreateRequest;
import bh.board.article.dto.ArticleResponse;

import bh.board.article.dto.ArticleUpdateRequest;


public interface ArticleService {
    ArticleResponse createArticle(ArticleCreateRequest request);
    ArticleResponse updateArticle(Long articleId, ArticleUpdateRequest request);
    ArticleResponse getArticle(Long articleId);
    void deleteArticle(Long articleId);
} 