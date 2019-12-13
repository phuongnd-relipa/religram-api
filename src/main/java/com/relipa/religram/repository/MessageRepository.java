package com.relipa.religram.repository;

import com.relipa.religram.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findAllByFromIdAndToId(Long fromId, Long toId);

    @Query(value = "select * from messages " +
            "where (from_id = :userId1 and to_id = :userId2) " +
            "or (from_id = :userId2 and to_id = :userId1) " +
            "order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<Message> getConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "select m1.* from messages m1 " +
            "inner join ( " +
            "select id, max(created_at) MAX_CREATED_AT, from_id, to_id " +
            "from messages " +
            "where from_id = :userId or to_id = :userId " +
            "group by from_id, to_id " +
            ") m2 " +
            "on (m1.from_id = m2.from_id or m1.to_id = m2.to_id) " +
            "and m1.created_at = m2.MAX_CREATED_AT " +
            "where m1.from_id = :userId or m1.to_id = :userId " +
            "order by m1.created_at desc", nativeQuery = true)
    List<Message> getChatList(@Param("userId") Long userId);
}
