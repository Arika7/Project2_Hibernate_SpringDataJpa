package ru.alishev.springcourse.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.repositories.BooksRepository;
import ru.alishev.springcourse.repositories.PersonRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BookServices {
    private  BooksRepository booksRepository;
    private PersonRepository personRepository;
    private final EntityManager entityManager;

    @Autowired
    public BookServices(BooksRepository booksRepository, PersonRepository personRepository, EntityManager entityManager) {
        this.booksRepository = booksRepository;
        this.personRepository = personRepository;

        this.entityManager = entityManager;
    }

    public List<Book> findAll(){return booksRepository.findAll();}

    public List<Book> findAllSorted(){
       return booksRepository.findAll(Sort.by("year"));
    }

    public Book findOne(int id){return booksRepository.findById(id).orElse(null);}

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }
    public Person getBookOwner(int id){
        Optional<Book> book = booksRepository.findById(id);
        if(book.isPresent()) return book.get().getOwner();
        else return null;
    }
    @Transactional
    public void delete(int id){ booksRepository.deleteById(id);}
    @Transactional
    public void assign(int id, Person person){
        Book book = findOne(id);
        if(person.getBooks() == null){
            person.setBooks(new ArrayList<>(Collections.singletonList(book)));
        }else person.getBooks().add(book);
        book.setOwner(person);
        book.setAssignTime(new Date());

    }

    @Transactional
    public void release(int id){
        Book book = entityManager.getReference(Book.class,id);
        book.getOwner().getBooks().remove(book);
        book.setOwner(null);
        book.setAssignTime(null);
    }

    public List<Book> findAll(Integer page, Integer books_per_page){
        return booksRepository.findAll(PageRequest.of(page,books_per_page)).getContent();
    }

    public List<Book> findAllAndSort(Integer page, Integer books_per_page){
        return booksRepository.findAll(PageRequest.of(page,books_per_page,Sort.by("year"))).getContent();
    }

    public Book findByNameStartingWith(String startWith){
        return booksRepository.findByNameStartingWith(startWith);
    }

}
