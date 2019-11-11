package ru.java.mentor.oldranger.club.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.java.mentor.oldranger.club.model.user.InvitationToken;
import ru.java.mentor.oldranger.club.model.user.User;
import ru.java.mentor.oldranger.club.service.mail.MailService;
import ru.java.mentor.oldranger.club.service.user.InvitationService;
import ru.java.mentor.oldranger.club.service.user.UserService;

@RestController
@RequestMapping("/api/invite")
public class InvitationTokenRestController {

    private InvitationService invitationService;

    private UserService userService;

    private MailService mailService;

    @Autowired
    public void setMailService(MailService service) {
        this.mailService = service;
    }

    @Autowired
    public void setUserService(UserService service) {
        this.userService = service;
    }

    @Autowired
    public void setInvitationService(InvitationService service) {
        this.invitationService = service;
    }

    @PostMapping
    public String saveInvitationToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.getUserByNickName(name);
        String key = invitationService.getCurrentKey(user);
        if (key == null) {
            key = invitationService.generateKey();
            InvitationToken invitationToken = new InvitationToken(key, user);
            invitationService.save(invitationToken);
        }
        return key;
    }

    @RequestMapping(value = "/onmail", method = RequestMethod.POST)
    public String sendInviteOnMail(@RequestBody String mail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.getUserByNickName(name);
        String message = "Привет! Это приглашение на форум \"Фашисты, свастика и всё такое\". Для регистрации пройди по ссылке:\n";
        String key = invitationService.generateKey();
        String link = "<a href=\"http://localhost:8888/invite?key=" + key + "\">http://localhost:8888/invite?key=" + key + "</a>";
        InvitationToken invitationToken = new InvitationToken(key, user, mail);
        String status = mailService.sendHtmlEmail(mail, name, message + link);
        invitationService.markInviteOnMailAsUsed(mail);
        invitationService.save(invitationToken);
        return status;
    }
}
