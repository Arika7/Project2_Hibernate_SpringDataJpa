package ru.alishev.springcourse.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.services.BookServices;
import ru.alishev.springcourse.services.PeopleServices;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookServices bookServices;
    private final PeopleServices peopleServices;

    public BookController(BookServices bookServices, PeopleServices peopleServices) {
        this.bookServices = bookServices;
        this.peopleServices = peopleServices;
    }

    @GetMapping()
    public String index(Model model,@RequestParam(value = "page", required = false) Integer page,@RequestParam(value = "books_per_page", required = false) Integer books_per_page,
                        @RequestParam(value = "sortByYear", required = false) Boolean sortByYear){

        if(page!=null & books_per_page!=null& sortByYear== null){
            model.addAttribute("books", bookServices.findAll(page,books_per_page));
        }else if(page!=null & books_per_page!=null & sortByYear!=null){
            model.addAttribute("books", bookServices.findAllAndSort(page,books_per_page));

        }else if(page == null & books_per_page == null & sortByYear!=null){
            model.addAttribute("books", bookServices.findAllSorted());
        }else model.addAttribute("books", bookServices.findAll());

        return "books/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id , Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookServices.findOne(id));

        Person bookOwner = bookServices.getBookOwner(id);
        if(bookOwner != null){
            model.addAttribute("owner", bookOwner);

        }else model.addAttribute("people", peopleServices.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book){

        bookServices.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookServices.findOne(id));
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, @PathVariable("id") int id){

        bookServices.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookServices.release(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookServices.assign(id, person);
        return "redirect:/books";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookServices.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model,@RequestParam(value = "gsearch", required = false, defaultValue = "") String search){
        if(search.isEmpty()) model.addAttribute("nothing","");
        else if (bookServices.findByNameStartingWith(search) == null) {
            model.addAttribute("nothingFound","");
        } else {
            Book book = bookServices.findByNameStartingWith(search);
            model.addAttribute("book",book);
            if(book.getOwner() != null) {model.addAttribute("owner", book.getOwner());
            }else model.addAttribute("free","");
        }
        return "books/search";
    }
}
