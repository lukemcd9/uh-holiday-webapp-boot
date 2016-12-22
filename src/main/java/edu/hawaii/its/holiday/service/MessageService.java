package edu.hawaii.its.holiday.service;

import edu.hawaii.its.holiday.type.Message;

public interface MessageService {

    public Message findMessage(int id);

    public Message add(Message message);

    public Message update(Message message);

}
