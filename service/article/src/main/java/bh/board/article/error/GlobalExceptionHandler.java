package bh.board.article.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //404 엔티티 없음
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
        decorate(pd,"entity_not_found",req);
        return pd;
    }

    private void decorate(ProblemDetail pd, String code, HttpServletRequest req) {
        pd.setType(URI.create("about:blank"));
        pd.setTitle(pd.getTitle() == null ? code : pd.getTitle());
        pd.setProperty("code", code);
        pd.setProperty("method", req.getMethod());
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
    }

}
