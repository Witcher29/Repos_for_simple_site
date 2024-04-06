package com.real.restfulsome;

import com.real.restfulsome.domain.Message;
import com.real.restfulsome.repos.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class BrowseController {
    @Autowired
    private MessageRepos messageRepos;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepos.findAll();

        model.put("messagesOfmain", messages);

        return "main";
    }

    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag);

        messageRepos.save(message);

        Iterable<Message> messages = messageRepos.findAll();

        model.put("messagesOfmain", messages);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepos.findByTag(filter);
        } else {
            messages = messageRepos.findAll();
        }

        model.put("messagesOfmain", messages);

        return "main";
    }
}

//@Service
//class StringServiceImpl implements StringService {
//    @Override
//    public String produceStr(String name) {
//        return "Привет из сервиса " + name;
//    }
//}
//interface StringService {
//    String produceStr(String name);
//}