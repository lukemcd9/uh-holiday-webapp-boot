package edu.hawaii.its.holiday.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hawaii.its.holiday.repository.MessageRepository;
import edu.hawaii.its.holiday.type.Message;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @CacheEvict(value = "messages", allEntries = true)
    public void evictCache() {
        // Empty.
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "messages", key = "#id")
    public Message findMessage(Integer id) {
        return messageRepository.findById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "messages", key = "#result.id")
    public Message update(Message message) {
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    @CachePut(value = "messages", key = "#message.id")
    public Message add(Message message) {
        return messageRepository.save(message);
    }

}