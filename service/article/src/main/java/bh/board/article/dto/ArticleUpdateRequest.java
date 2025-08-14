package bh.board.article.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
@ToString
public class ArticleUpdateRequest {
    private String title;
    private String content;
}

