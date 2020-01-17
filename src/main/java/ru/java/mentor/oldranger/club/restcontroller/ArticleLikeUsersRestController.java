package ru.java.mentor.oldranger.club.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.mentor.oldranger.club.model.article.Article;
import ru.java.mentor.oldranger.club.model.user.User;
import ru.java.mentor.oldranger.club.service.article.ArticleService;
import ru.java.mentor.oldranger.club.service.utils.SecurityUtilsService;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("api/article")
@Tag(name = "ArticleLike")
public class ArticleLikeUsersRestController {


    private ArticleService articleService;
    private SecurityUtilsService securityUtilsService;

    @Operation(security = @SecurityRequirement(name = "security"),
            summary = "Set likes users or dislikes users", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @PostMapping(value = "/{articleId}/like")
    public void makeLike(@PathVariable long articleId) {
        User user = securityUtilsService.getLoggedUser();
        if (!articleService.isUserLikedtest(articleId, user.getId()).isEmpty()) {
            articleService.removeUser(articleId, user.getId());
        } else {
            articleService.addUser(articleId, user.getId());
        }

    }


    @Operation(security = @SecurityRequirement(name = "security"),
            summary = "Get likes users article", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @GetMapping(value = "/{articleId}/likes")
    public long getLikeUsersByArticlesId(@PathVariable long articleId) {
        return articleService.countLikes(articleId);
    }

}