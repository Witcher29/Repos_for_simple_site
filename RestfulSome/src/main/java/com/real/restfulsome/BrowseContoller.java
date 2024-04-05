package com.real.restfulsome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("yeah/")
public class BrowseContoller {
    private StringService stringService;
    @Autowired
    public BrowseContoller(StringService stringService){
        this.stringService = stringService;
    }
    @GetMapping("getName/{name}")
    public String saySomth(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
@Service
class StringServiceImpl implements StringService {
    @Override
    public String produceStr(String name) {
        return "Привет из сервиса " + name;
    }
}
interface StringService {
    String produceStr(String name);
}