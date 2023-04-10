package ru.alishev.springcourse.services;


import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.repositories.BooksRepository;
import ru.alishev.springcourse.repositories.PersonRepository;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleServices {
   private PersonRepository personRepository;
   private BooksRepository booksRepository;
   private EntityManager entityManager;
    @Autowired
    public PeopleServices(PersonRepository personRepository, BooksRepository booksRepository, EntityManager entityManager) {
        this.personRepository = personRepository;
        this.booksRepository = booksRepository;
        this.entityManager = entityManager;
    }

    public PeopleServices() {
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findOne(int id){
        return personRepository.findById(id).orElse(null);
    }
    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

    public List<Book> getBooks(int person_id){
        Optional<Person> person = personRepository.findById(person_id);
        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        }else return Collections.emptyList();
    }

}
