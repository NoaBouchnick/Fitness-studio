package gym.customers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person {

    private static Map<Integer, Integer> balancesMap = new HashMap<>();
    private String name;
    private String data;
    private Gender gender;
    private int age;
    private static int idCounter = 1111;
    private int id;


    public Person(String name, int initialBalance, Gender gender, String data) {
        this.name = name;
        this.data = data;
        this.gender = gender;
        this.age = calculateAgeFromData(data);

        this.id = getIdCounter();
        balancesMap.put(this.id, initialBalance);
    }

    protected Person(Person other) {
        this.name = other.name;
        this.data = other.data;
        this.gender = other.gender;
        this.age = other.age;
        this.id = other.id;
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
        return balancesMap.getOrDefault(this.id, 0);
    }

    public void setMoneyBalance(int newBalance) {
        balancesMap.put(this.id, newBalance);
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

    public void deductMoney(int amount) {
        int currentBalance = getMoneyBalance();
        if (amount > currentBalance) {
            throw new IllegalArgumentException(
                    "Insufficient funds: Cannot deduct " + amount + " from balance " + currentBalance
            );
        }
        setMoneyBalance(currentBalance - amount);
    }

    public boolean isInstructor() {
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getMoneyBalance() == person.getMoneyBalance() &&
                Objects.equals(name, person.name) &&
                Objects.equals(data, person.data) &&
                gender == person.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getMoneyBalance(), data, gender);
    }

    @Override
    public String toString() {
        return "gym.customers.Person{" +
                "name='" + name + '\'' +
                ", balance=" + getMoneyBalance() +
                ", data=" + data +
                ", gender=" + gender +
                '}';
    }
}
