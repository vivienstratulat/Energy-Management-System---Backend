package com.example.chat.services;

import com.example.chat.entities.Message;
import com.example.chat.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        System.out.println("Saving message: " + message);
        return messageRepository.save(message);
    }

    public List<Message> getMessages(int userId, int otherUserId) {
        List<Message> sentMessages = messageRepository.findAllByFromIdAndToId(userId, otherUserId);
        List<Message> receivedMessages = messageRepository.findAllByFromIdAndToId(otherUserId, userId);
        return Stream.concat(sentMessages.stream(), receivedMessages.stream())
                .collect(Collectors.toList());
    }


    public List<Integer> getConversations(int id) {
        // Fetch messages received by the user
        List<Message> receivedMessages = messageRepository.findAllByToId(id);
        List<Integer> usersReceivedFrom = receivedMessages.stream()
                .map(Message::getFromId)
                .distinct()
                .collect(Collectors.toList());

        // Fetch messages sent by the user
        List<Message> sentMessages = messageRepository.findAllByFromId(id);
        List<Integer> usersSentTo = sentMessages.stream()
                .map(Message::getToId)
                .distinct()
                .collect(Collectors.toList());

        // Combine both lists and remove duplicates
        return Stream.concat(usersReceivedFrom.stream(), usersSentTo.stream())
                .distinct()
                .collect(Collectors.toList());
    }


   /* public boolean markAsSeen(int messageId) {
        Message message = messageRepository.findById(messageId);
        System.out.println("puttin seen Message: " + message);
        if (message != null) {
            message.setSeen(true);
            messageRepository.save(message);
            return true;
        }
        else {
            System.out.println("Message not found");
        }
        return false;
    }*/

    public int markAsSeen(int messageId) {
        Message message = messageRepository.findById(messageId);
        System.out.println("puttin seen Message: " + message);
        if (message != null) {
            message.setSeen(true);
            messageRepository.save(message);
            System.out.println("Message seen in the db???");
            return message.getFromId();
        }
        else {
            System.out.println("Message not found");
        }
        return -1;
    }


    public Message getMessageById(int id) {
        Message message = messageRepository.findById(id);
        if (message != null) {
            System.out.println("Message found: " + message);
            return message;
        }
        else {
            System.out.println("Message not found");
        }
        return null;
    }
}
