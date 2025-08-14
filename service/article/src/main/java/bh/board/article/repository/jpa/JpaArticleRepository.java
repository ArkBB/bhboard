package bh.board.article.repository.jpa;

import bh.board.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaArticleRepository extends JpaRepository<Article, Long> {
}