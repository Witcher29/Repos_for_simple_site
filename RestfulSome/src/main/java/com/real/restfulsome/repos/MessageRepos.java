package com.real.restfulsome.repos;

import com.real.restfulsome.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepos extends JpaRepository<Message, Long> {
    List<Message> findByTag(String tag);
    List<Message> findMessageByText(String text);
}
