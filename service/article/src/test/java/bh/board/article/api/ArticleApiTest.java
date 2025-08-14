package bh.board.article.api;

import bh.board.article.dto.ArticleCreateRequest;
import bh.board.article.dto.ArticleResponse;
import bh.board.article.dto.ArticleUpdateRequest;
import bh.board.article.repository.jpa.JpaArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class ArticleApiTest {

    @LocalServerPort
    int port;

    @Autowired
    RestClient.Builder restBuilder;

    @Autowired
    JpaArticleRepository jpaRepo; // DB 상태 직접 확인용

    private RestClient rest() {
        return restBuilder.baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("Article CRUD 통합 흐름 (create→read→update→delete)")
    void crud_flow() {
        // 1) Create
        ArticleCreateRequest createReq = new ArticleCreateRequest("hi", "my content", 1L, 1L);
        ArticleResponse created = rest().post()
                .uri("/api/articles")
                .body(createReq)
                .retrieve()
                .body(ArticleResponse.class);

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("hi");
        assertThat(created.getContent()).isEqualTo("my content");
        assertThat(created.getArticleId()).isNotNull();

        Long id = created.getArticleId();

        // 2) Read
        ArticleResponse found = rest().get()
                .uri("/api/articles/{id}", id)
                .retrieve()
                .body(ArticleResponse.class);

        assertThat(found).isNotNull();
        assertThat(found.getArticleId()).isEqualTo(id);
        assertThat(found.getTitle()).isEqualTo("hi");
        assertThat(found.getContent()).isEqualTo("my content");

        // (선택) 레포지토리로 DB 상태 직접 확인
        assertThat(jpaRepo.findById(id)).isPresent();

        // 3) Update
        ArticleUpdateRequest updateReq = new ArticleUpdateRequest("hi 2", "my content 2");
        ArticleResponse updated = rest().put()
                .uri("/api/articles/{id}", id)
                .body(updateReq)
                .retrieve()
                .body(ArticleResponse.class);

        assertThat(updated).isNotNull();
        assertThat(updated.getArticleId()).isEqualTo(id);
        assertThat(updated.getTitle()).isEqualTo("hi 2");
        assertThat(updated.getContent()).isEqualTo("my content 2");

        // 4) Delete
        rest().delete()
                .uri("/api/articles/{id}", id)
                .retrieve()
                .body(ArticleResponse.class);

        // 삭제 확인: 404 기대
        try {
            rest().get().uri("/api/articles/{id}", id).retrieve().body(ArticleResponse.class);
            fail("삭제 후 조회는 404가 되어야 합니다.");
        } catch (RestClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            assertThat(e.getStatusCode().value()).isEqualTo(404);
        }

        //레포지토리로 최종 확인
        assertThat(jpaRepo.findById(id)).isNotPresent();
    }
}
