package com.example.chat.controllers;

import com.example.chat.entities.Person;
import com.example.chat.entities.TypingNotification;
import com.example.chat.services.MessageService;
import com.example.chat.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import com.example.chat.entities.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final MessageService messageService;
    private final PersonService personService;

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        try {
            System.out.println("Received message text: " + message.getText());
            System.out.println("Received message to: " + message.getToId());
            System.out.println("Received message: " + message);
          //  simpMessagingTemplate.convertAndSendToUser(
            //        String.valueOf(message.getTo_id()), "/queue/messages", message);

          Message savedMessage=  messageService.saveMessage(message);
            simpMessagingTemplate.convertAndSend("/queue/"+message.getToId(), savedMessage);
        } catch (MessagingException e) {
            System.err.println("Error sending message: " + e.getMessage());
            // Handle or log the exception appropriately
        }
    }

    // In a @Controller class

    @MessageMapping("/typing")
    public void handleTypingEvent(@RequestHeader TypingNotification typingNotification) {
        // Broadcast this event to the specific user's queue
        simpMessagingTemplate.convertAndSend("/queue/typing/" + typingNotification.getToId(), typingNotification);
    }

    @MessageMapping("/seen")
    public void handleSeenEvent(int messageId) {
        System.out.println("am ajuns la seen???");
        int senderId = messageService.markAsSeen(messageId);
        if (senderId != -1) {
            // Notify the sender that the message has been seen
            simpMessagingTemplate.convertAndSend("/queue/seen/" + senderId, messageId);
        }
        else{
            System.out.println("Sender id is -1");
        }
    }



    @GetMapping(value = "/getMessages/{fromId}/{toId}")
    public ResponseEntity< List<Message>> getMessages(@PathVariable("fromId")  int fromId,@PathVariable("toId") int toId) {

        List<Message> messages= messageService.getMessages(fromId, toId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping(value = "/saveMessage")
    public ResponseEntity< Message> saveMessage(@RequestBody Message message) {
      Message savedMessage=  messageService.saveMessage(message);
        return new ResponseEntity<>( savedMessage,HttpStatus.OK);
    }

    @GetMapping(value="/conversations/{id}")
    public ResponseEntity<List<Integer>> getConversations(@PathVariable("id") int id){
        List<Integer> conversations = messageService.getConversations(id);
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping(value="/getMessageById/{id}")
    public ResponseEntity<String> getMessageById(@PathVariable("id") int id){
        Message message = messageService.getMessageById(id);
        String messageText=message.getText();
        return new ResponseEntity<>(messageText, HttpStatus.OK);
    }
   /* @PostMapping("/markMessageAsSeen/{messageId}")
    public ResponseEntity<?> markMessageAsSeen(@PathVariable("messageId") int messageId) {
        System.out.println("Message id: " + messageId);
            boolean flag=messageService.markAsSeen(messageId);
            if (flag) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }*/

    /*@PostMapping("/markMessageAsSeen/{messageId}")
    public ResponseEntity<?> markMessageAsSeen(@PathVariable("messageId") int messageId) {
        System.out.println("Message id: " + messageId);
        int senderId = messageService.markAsSeen(messageId);
        if (senderId != -1) {
            // Send seen notification to the sender
            simpMessagingTemplate.convertAndSend("/queue/seen/" +senderId, messageId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @GetMapping(value = "/getPersonById/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable("id") int personId) {
        Person person = personService.findById(personId);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }


}
