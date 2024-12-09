package gym.customers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Person {
    private String name;
    private int financialBalance;
    private String data;
    private Gender gender;
    private int age;

    public Person(String name, int financialBalance, Gender gender, String data) {
        this.name = name;
        this.financialBalance = financialBalance;
        this.data = data;
        this.gender = gender;
        this.age = calculateAgeFromData(data);
    }

    public Person() {
    }

    private int calculateAgeFromData(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(data, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }


    public int getFinancialBalance() {
        return financialBalance;
    }

    public String getData() {
        return data;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return financialBalance == person.financialBalance && Objects.equals(name, person.name) && Objects.equals(data, person.data) && gender == person.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, financialBalance, data, gender);
    }

    @Override
    public String toString() {
        return "gym.customers.Person{" +
                "name='" + name + '\'' +
                ", balance=" + financialBalance +
                ", data=" + data +
                ", gender=" + gender +
                '}';
    }
}
