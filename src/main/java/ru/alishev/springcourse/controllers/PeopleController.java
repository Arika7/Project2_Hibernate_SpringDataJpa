package ru.alishev.springcourse.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.services.PeopleServices;
import ru.alishev.springcourse.util.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleServices peopleServices;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleServices peopleServices, PersonValidator personValidator) {
        this.peopleServices = peopleServices;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model){
        // Получим всех людей и передадим в view
        model.addAttribute("people", peopleServices.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        //Получим одного человка по id и поместим его в model и передадим в view
       model.addAttribute("person", peopleServices.findOne(id));
       model.addAttribute("books", peopleServices.getBooks(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
    personValidator.validate(person, bindingResult);
    if(bindingResult.hasErrors()) return "people/new";
    peopleServices.save(person);
    return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person",peopleServices.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) return "/people/edit";

        peopleServices.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id){
        peopleServices.delete(id);
        return "redirect:/people";
    }
}
