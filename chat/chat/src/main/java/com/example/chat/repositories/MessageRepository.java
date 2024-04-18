package com.example.chat.repositories;

import com.example.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    public List<Message> findAllByFromIdAndToId(int from_id, int to_id);
    public List<Message> findAllByFromId(int id);
    List<Message> findAllByToId(int toId);
    Message findById(int id);

}
