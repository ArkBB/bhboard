package bh.board.article.service;

import bh.board.article.dto.ArticleCreateRequest;
import bh.board.article.dto.ArticleResponse;
import bh.board.article.dto.ArticleUpdateRequest;
import bh.board.article.entity.Article;
import bh.board.article.repository.jpa.ArticleRepositoryAdapter;
import bh.board.common.snowflake.Snowflake;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepositoryAdapter articleRepository;
    private final Snowflake snowflake = new Snowflake();
    

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        Article article = Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getWriterId(), request.getBoardId());
        
        Article savedArticle = articleRepository.save(article);
        return ArticleResponse.from(savedArticle);
    }

    @Override
    @Transactional
    public ArticleResponse updateArticle(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + articleId));

        article.update(request.getTitle(), request.getContent());
        Article updatedArticle = articleRepository.save(article);
        return ArticleResponse.from(updatedArticle);
    }


    @Override
    public ArticleResponse getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + articleId));
        return ArticleResponse.from(article);
    }


    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new IllegalArgumentException("Article not found with id: " + articleId);
        }
        articleRepository.deleteById(articleId);
    }
    

} 