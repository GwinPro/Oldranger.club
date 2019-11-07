package ru.java.mentor.oldranger.club.dao.ForumRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.java.mentor.oldranger.club.dto.TopicAndNewMessagesCountDto;
import ru.java.mentor.oldranger.club.model.forum.Section;
import ru.java.mentor.oldranger.club.model.forum.Topic;
import ru.java.mentor.oldranger.club.model.user.User;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    /**
     * Выборка по N первых актуальных (соритировка по дате) Topic (из topics) для каждой Section.
     */
    @Query(nativeQuery = true,
            value = "select * " +
                    "from (select *, row_number() over (partition by id_section order by date_last_message desc, id asc) i from topics) t where i <= ?1")
    List<Topic> getActualTopicsLimitAnyBySection(Integer limitTopicsBySection);

    /**
     * Выборка по N первых актуальных (соритировка по дате) Topic (из topics) для каждой Section.
     * Topic выбираются с условием isHideToAnon = false (значение isHideToAnon в Section не учитывается)
     */
    @Query(nativeQuery = true,
            value = "select * " +
                    "from (select *, row_number() over (partition by id_section order by date_last_message desc, id asc) i from topics where is_hide = false) t where i <= ?1")
    List<Topic> getActualTopicsLimitAnyBySectionForAnon(Integer limitTopicsBySection);

    /**
     * Подмножество Topics для выбранной Subsection, которые помечены для анонимов.
     * Отсортированно по дате последнего сообщения в Topic.
     */
    Page<Topic> findBySectionAndIsHideToAnonIsFalseOrderByLastMessageTimeDesc(Section section, Pageable pageable);

    /**
     * Используется для неанонимов.<br>
     * Возвращает подмножество Topics для выбранной id Subsection с эмуляцией пагинации.<br>
     * Порядок сортировки:<br>
     * 1. Элементы с подпиской пользователя, где есть новые сообщения &ndash; сверху.<br>
     * 2. Общая сортировка по дате последнего сообщения (свежие сверху).<br>
     * <b>Важно</b>: Pageable в чистом виде с таким запросом не работает.
     * Поэтому используется дополнительный метод<br>
     * {@code long countForGetSliceListBySubsectionForUserOrderByLastMessageTimeDescAndSubscriptionsWithNewMessagesFirst}
     * чтобы получить общее количество элементов Topics для эмуляции пейджинга.
     */
    @Query(nativeQuery = true,
            value = "select *, (comb.id_user is not null and (comb.date_last_message>=comb.date_lastvisit or comb.date_lastvisit is null)) as subscriber_has_new_msg from " +
                    "(select * from topics t left join (select id_topic, id_user, date_lastvisit from subscriptions s where s.id_user=?1 group by s.id_topic) st on t.id=st.id_topic " +
                    "where t.id_section=?2) as comb " +
                    "order by subscriber_has_new_msg desc, comb.date_last_message desc " +
                    "limit ?3,?4")
    List<Topic> getSliceListBySubsectionForUserOrderByLastMessageTimeDescAndSubscriptionsWithNewMessagesFirst(long userId, long sectionId, int offset, int limit);

    /**
     * Подсчёт общего количества элементов для выборок<br>
     * {@code List<Topic> getSliceListBySubsectionForUserOrderByLastMessageTimeDescAndSubscriptionsWithNewMessagesFirst}
     */
    @Query(nativeQuery = true,
            value = "select count(*) from " +
                    "(select * from topics t left join " +
                    "(select id_topic, id_user, date_lastvisit from subscriptions s where s.id_user=?1 group by s.id_topic) " +
                    "st on t.id=st.id_topic where t.id_section=?2) as comb")
    long countForGetSliceListBySubsectionForUserOrderByLastMessageTimeDescAndSubscriptionsWithNewMessagesFirst(long userId, long sectionId);
}