package ru.java.mentor.oldranger.club.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.java.mentor.oldranger.club.model.user.User;
import ru.java.mentor.oldranger.club.model.user.UserAvatar;
import ru.java.mentor.oldranger.club.service.user.UserAvatarService;
import ru.java.mentor.oldranger.club.service.user.UserService;
import ru.java.mentor.oldranger.club.service.utils.SecurityUtilsService;

@RestController
@RequestMapping("/api/avatar")
@Tag(name = "Avatar from user")
public class AvatarUserRestController {

    @Autowired
    private UserAvatarService userAvatarService;

    @Autowired
    private SecurityUtilsService securityUtilsService;

    @Operation(security = @SecurityRequirement(name = "security"),
            summary = "Set Avatar ", description = "Set avatar to user ", tags = { "Avatar from user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping(value = "/set", produces = { "application/json" })
    public ResponseEntity<String> setAvatarToUser(@RequestParam("file") MultipartFile file){
        User user = securityUtilsService.getLoggedUser();
        if (user == null) {
            return ResponseEntity.noContent().build();
        }

        try {
            if (user.getAvatar() == null) {
                userAvatarService.setAvatarToUser(user, file);
            } else {
                userAvatarService.updateUserAvatar(user, file);
            }
        } catch (Exception o) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("аватар загружен");
    }

    @Operation(security = @SecurityRequirement(name = "security"),
            summary = "Delete Avatar ", description = "Delete avatar", tags = { "Avatar from user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping(value = "/delete", produces = { "application/json" })
    public ResponseEntity<String> setAvatarToUser(){
        User user = securityUtilsService.getLoggedUser();
        if (user == null) {
            return ResponseEntity.noContent().build();
        }

        try {
            if (user.getAvatar() != null) {
                userAvatarService.deleteUserAvatar(user);
            } else {
                ResponseEntity.noContent().build();
            }
        } catch (Exception o) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("удален");
    }
}
