package ru.alishev.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


@Entity
@Table(name = "People")
public class Person {


    @Id
    @Column(name = "id")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+", message = "Имя Фимилия Отчество")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "fio")
    @NotEmpty(message = "Имя не может быть пустым")
    String FIO;

    @Column(name = "year")
    @NotNull(message = "Напишите ваш год рождения")
    @Min(value = 1870 , message = "Неверный год рождения")
    @Max(value = 2006, message = "Вы не можете взять книгу(18+)")
    int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;


    public Person(String FIO, int year) {
        this.FIO = FIO;
        this.year = year;
    }

    public Person(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", FIO='" + FIO + '\'' +
                ", year=" + year +
                '}';
    }
}
