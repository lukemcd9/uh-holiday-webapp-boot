package edu.hawaii.its.holiday.service;

import edu.hawaii.its.holiday.type.Message;

public interface MessageService {

    void evictCache();

    Message findMessage(Integer id);

    Message add(Message message);

    Message update(Message message);

}
