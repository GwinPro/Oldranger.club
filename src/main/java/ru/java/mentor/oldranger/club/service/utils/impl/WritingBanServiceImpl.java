package ru.java.mentor.oldranger.club.service.utils.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.java.mentor.oldranger.club.dao.WritingBanRepository;
import ru.java.mentor.oldranger.club.model.user.User;
import ru.java.mentor.oldranger.club.model.utils.BanType;
import ru.java.mentor.oldranger.club.model.utils.WritingBan;
import ru.java.mentor.oldranger.club.service.utils.WritingBanService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class WritingBanServiceImpl implements WritingBanService {

    private static final Logger LOG = LoggerFactory.getLogger(WritingBanServiceImpl.class);
    WritingBanRepository repository;

    @Override
    public WritingBan getByUserAndType(User user, BanType type) {
        LOG.debug("Getting writing ban for user with id = {}", user.getId());
        WritingBan ban = null;
        try {
            ban = repository.findByUserAndBanType(user, type);
            LOG.debug("Writing ban returned");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return ban;
    }

    @Override
    public void save(WritingBan writingBan) {
        LOG.info("Saving writing ban {}", writingBan);
        try {
            repository.save(writingBan);
            LOG.info("Writing ban saved");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isForbidden(User user, BanType type) {
        boolean isForbidden = false;
        if (user == null) {
            isForbidden = true;
        } else {
            WritingBan writingBan = repository.findByUserAndBanType(user, type);
            if (writingBan != null && (writingBan.getUnlockTime() == null || writingBan.getUnlockTime().isAfter(LocalDateTime.now()))) {
                isForbidden = true;
            }
        }
        return isForbidden;
    }
}
