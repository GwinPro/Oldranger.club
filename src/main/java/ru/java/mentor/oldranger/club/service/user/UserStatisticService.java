package ru.java.mentor.oldranger.club.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.java.mentor.oldranger.club.model.user.UserStatistic;

public interface UserStatisticService {

    UserStatistic getUserStaticById(Long id);

    void saveUserStatic(UserStatistic userStatistic);

    Page<UserStatistic> getAllUserStatistic(Pageable pageable);
}
