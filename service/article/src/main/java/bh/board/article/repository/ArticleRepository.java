package bh.board.article.repository;

import bh.board.article.entity.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);
    Optional<Article> findById(Long articleId);
    void deleteById(Long articleId);
    boolean existsById(Long articleId);
} 