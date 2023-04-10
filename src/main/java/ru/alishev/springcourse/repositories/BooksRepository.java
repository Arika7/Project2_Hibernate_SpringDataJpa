package ru.alishev.springcourse.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.models.Book;

import java.util.List;



@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
  public List<Book> getBooksByOwnerId(int id);

  Page<Book> findAll(Pageable var1);

  List<Book> findAll(Sort var1);

  Book findByNameStartingWith(String startWith);
}
