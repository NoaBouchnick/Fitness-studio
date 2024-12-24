package gym.customers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.Objects;

public class Person {
    private String name;
    private int moneyBalance;
    private String data;
    private Gender gender;
    private int age;
    private static int idCounter = 1111;
    private int id;


    public Person(String name, int moneyBalance, Gender gender, String data) {
        this.name = name;
        this.moneyBalance = moneyBalance;
        this.data = data;
        this.gender = gender;
        this.age = calculateAgeFromData(data);
        this.id = getIdCounter();
    }

    protected Person(Person other) {
        this.name = other.name;
        this.moneyBalance = other.moneyBalance;
        this.data = other.data;
        this.gender = other.gender;
        this.age = other.age;
        this.id = other.id;  // חשוב: שומר על אותו ID!
    }

    public synchronized static int getIdCounter() {
        return idCounter++;
    }

    public int getId() {
        return id;
    }

    private int calculateAgeFromData(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(data, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }


    public int getMoneyBalance() {
        return moneyBalance;
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

    public void setMoneyBalance(int moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public int deductMoney(int money) {
        if (money > getMoneyBalance()) {
            throw new IllegalArgumentException("Insufficient funds: Cannot deduct " + money + " from balance " + getMoneyBalance());
        }
        setMoneyBalance(getMoneyBalance() - money);
        return getMoneyBalance();
    }

    public boolean isInstructor() {
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return moneyBalance == person.moneyBalance && Objects.equals(name, person.name) && Objects.equals(data, person.data) && gender == person.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, moneyBalance, data, gender);
    }

    @Override
    public String toString() {
        return "gym.customers.Person{" +
                "name='" + name + '\'' +
                ", balance=" + moneyBalance +
                ", data=" + data +
                ", gender=" + gender +
                '}';
    }
}
