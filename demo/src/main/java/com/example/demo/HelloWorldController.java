package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {
    @RequestMapping(value = "/greeting")// Данная аннотация говорит нам о том, что данный метод обрабатывает HTTP GET запросы на адрес /greeting.
    public String helloWorldController(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";// Согласно Spring-MVC, метод контроллера должен вернуть имя представления. Далее Spring будет искать html файл, с таким именем, который и вернет в качестве ответа на HTTP запрос. Как ты видишь, наш метод возвращает имя веб страницы, которую мы создали ранее - greeting.
    }
}
// @RequestParam(name = "name", required = false, defaultValue = "World") String name.
// Аннотация @RequestParam говорит о том, что параметр String name - является параметром url.
// В скобках аннотации указано, что данный параметр в url является не обязательным (required = false),
// в случае его отсутствия, значением параметра String name будет World (defaultValue = "World"), а если он будет присутствовать,
// то данный параметр в url будет именоваться name (name = "name")

//Вторым параметром является Model model. Данный параметр является некоторой моделью.
// Данная модель состоит внутри из различных атрибутов. Каждый атрибут имеет имя и значение. Что-то вроде пар ключ-значение.
// С помощью данного параметра мы можем передавать данные из Java кода в html страницы. Или же, говоря терминологией MVC,
// передавать данные из Модели (Model) в Представление (View).