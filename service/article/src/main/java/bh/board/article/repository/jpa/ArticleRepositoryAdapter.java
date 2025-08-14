// bh.board.article.repository.jpa.JpaArticleRepositoryImpl   ← 도메인 인터페이스 구현체
package bh.board.article.repository.jpa;

import bh.board.article.entity.Article;
import bh.board.article.repository.ArticleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile({"dev","test"})  // 실행시 활성화된 프로필이 여야 빈으로 등록
public class ArticleRepositoryAdapter implements ArticleRepository {
    private final JpaArticleRepository delegate;

    public ArticleRepositoryAdapter(JpaArticleRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Article save(Article article) {
        return delegate.save(article);
    }
    @Override
    public Optional<Article> findById(Long articleId) {
        return delegate.findById(articleId);
    }
    @Override
    public void deleteById(Long articleId) {
        delegate.deleteById(articleId);
    }
    @Override
    public boolean existsById(Long articleId) {
        return delegate.existsById(articleId);
    }
}
