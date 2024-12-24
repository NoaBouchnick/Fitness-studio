package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.List;

public class Secretary extends Person {

    private static List<Client> clients = new ArrayList<>();
    private static List<Instructor> instructors = new ArrayList<>();
    private static List<Session> sessions = new ArrayList<>();
    private static List<String> actionsHistory = new ArrayList<>();
    private static   Secretary currentSecretary = null;
    private boolean isActive = true;
    private int salary;

    public Secretary(String name, int wage, Gender gender, String date) {
        super(name, wage, gender, date);
        this.salary = wage;  // שימי לב לתיקון כאן
    }

    // בנאי "שדרוג" מתוך Person קיים (שומר על אותו ID)
    public Secretary(Person existingPerson, int wage) {
        super(existingPerson); // קורא לבנאי המוגן ב-Person ושומר על ה-ID
        this.salary = wage;
    }

    public int getSalary() {
        return salary;
    }

    public void deactivate() {
        isActive = false;
    }

    private boolean isAuthorized() {
        if (!isActive || Secretary.getCurrentSecretary() != this) {
            System.out.println("Error: Former secretaries are not permitted to perform actions");
            return false;
        }
        return true;
    }

    public List<Client> getClients() {
        return clients;
    }

    public static List<Session> getSessions() {
        return sessions;
    }

    public static List<Instructor> getInstructors() {
        return instructors;
    }

    public static Secretary getCurrentSecretary() {
        return Gym.getInstance().getSecretary();
    }

    public void addActionToHistory(String action) {
        actionsHistory.add(action);
    }

    public static void setCurrentSecretary(Secretary currentSecretary) {
        Secretary.currentSecretary = currentSecretary;
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException {
        if (!isAuthorized()) {
            return null;
        }
        try {
            if (p.getAge() < 18) {
                throw new InvalidAgeException("Client age must be 18 or older", p.getAge());
            }
            for (Client client : clients) {
                if (client.equals(new Client(p, new ArrayList<>(), new ArrayList<>()))) {
                    throw new DuplicateClientException("Client already registered", client);
                }
            }
            Client newClient = new Client(p, new ArrayList<>(), new ArrayList<>());
            clients.add(newClient);
            actionsHistory.add("Registered new client: " + p.getName());
            return newClient;

        } catch (InvalidAgeException e) {
            System.out.println("Error: Client must be at least 18 years old to register");
        } catch (DuplicateClientException e) {
            System.out.println("Error: The client is already registered");
        }
        return null;
    }

    public void unregisterClient(Client c) {
        if (!isAuthorized()) {
            return;
        }
        try {
            if (!clients.contains(c)) {
                throw new ClientNotRegisteredException("Registration is required before attempting to unregister", c);
            }
            clients.remove(c);
            c.addNotification("You have been unregistered from the gym.\n");
            actionsHistory.add("Unregistered client: " + c.getName());
        } catch (ClientNotRegisteredException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error while unregistering client: " + e.getMessage());
        }
    }


    public Instructor hireInstructor(Person p, int hourlyWage, List<SessionType> sessions) {
        if (!isAuthorized()) {
            return null;
        }
        try {
            if (sessions == null || sessions.isEmpty()) {
                throw new IllegalArgumentException("Instructor must be qualified for at least one session.");
            }
            Instructor newInstructor = new Instructor(p, hourlyWage, sessions);
            instructors.add(newInstructor);
            actionsHistory.add("Hired new instructor: " + p.getName() + " with salary per hour: " + hourlyWage );
            return newInstructor;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error while hiring instructor: " + e.getMessage());
            return null;
        }
    }


    public Session addSession(SessionType sessionType, String data, ForumType forumType, Instructor instructor)
            throws InstructorNotQualifiedException {
        if (!isAuthorized()) {
            return null;
        }
        try {
            if (!instructor.mayTeach(sessionType)) {
                throw new InstructorNotQualifiedException("Error: Instructor is not qualified to conduct this session type.");
            }
        } catch (InstructorNotQualifiedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime parsedDateTime = LocalDateTime.parse(data, inputFormatter);
            String formattedDate = parsedDateTime.format(outputFormatter);

            Session newSession = SessionFactory.createSession(sessionType, formattedDate, forumType, instructor);
            if (newSession != null) {
                sessions.add(newSession);
                actionsHistory.add("Created new session: " + sessionType + " on " + formattedDate + " with instructor: " + instructor.getName());
                instructor.getSessionsTaught().add(newSession);
                return newSession;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Expected format: dd-MM-yyyy HH:mm. Received: " + data);
            return null;
        }
        return null;
    }

    public boolean registerClientToLesson(Client c, Session session) {
        if (!isAuthorized()) {
            return false;
        }
        boolean canRister = true;
        try {

            if (!clients.contains(c)) {
                throw new ClientNotRegisteredException("The client is not registered with the gym and cannot enroll in lessons", c);
            }
            if (session.getClientsInSession().contains(c)) {
                throw new DuplicateClientException("The client is already registered for this lesson", c);
            }
            if (session.getSessionDate().isBefore(LocalDateTime.now())) {
                actionsHistory.add("Failed registration: Session is not in the future");
                canRister = false;
            }
            int sessionPrice = session.getPrice();

            for (Instructor instructor : instructors) {
                if (instructor.getName().equals(c.getName())) {
                    if (instructor.isTeachingAtTime(session.getSessionDate())) {
                        if (!session.getInstructor().getName().equals(c.getName())) {
                            break;
                        }
                    }
                }
            }

            if (session.isFull()) {
                actionsHistory.add("Failed registration: No available spots for session");
                return false;
            }
            ForumType forumType = session.getForumType();
            switch (forumType) {
                case All:
                    break;
                case Seniors:
                    if (c.getAge() < 65) {
                        actionsHistory.add("Failed registration: Client doesn't meet the age requirements for this session (Seniors)");
                        canRister = false;
                    }
                    break;
                case Male:
                    if (c.getGender() != Gender.Male) {
                        actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements");
                        canRister = false;
                    }
                    break;
                case Female:
                    if (c.getGender() != Gender.Female) {
                        actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements");
                        canRister = false;
                    }
                    break;
            }

            if (c.getMoneyBalance() < sessionPrice) {
                actionsHistory.add("Failed registration: Client doesn't have enough balance");
                canRister = false;
            }
            if (!canRister) {
                return false;
            }
            session.addClient(c);
            c.deductMoney(sessionPrice);
            c.addSessionClient(session);

            Gym gym = Gym.getInstance();
            gym.setBalance(gym.getBalance() + sessionPrice);

            actionsHistory.add("Registered client: " + c.getName() +
                    " to session: " + session.getSessionType() +
                    " on " + session.getSessionDate() +
                    " for price: " + session.getPrice()); // Log the action
            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public int paySalaries() {
        int totalPayment = 0;

        for (Instructor instructor : instructors) {
            int numOfSessions = instructor.getSessionsTaught().size();
            int hourlyWage = instructor.getHourlyWage();

            int salary = numOfSessions * hourlyWage;
            instructor.setMoneyBalance(instructor.getMoneyBalance() + salary);

            totalPayment += salary;
        }
            int secretarySalary = getSalary();
            totalPayment += secretarySalary;
            setMoneyBalance(getMoneyBalance() + secretarySalary);

        Gym gym = Gym.getInstance();
        gym.setBalance(gym.getBalance() - totalPayment);
        actionsHistory.add("Salaries have been paid to all employees");
        return totalPayment;
    }

    public void printActions() {
        for (String action : actionsHistory) {
            System.out.println(action);
        }
    }

    public void notify(Session session, String message) {
        if (!isAuthorized()) {
            return;
        }

        for (Client client : session.getClientsInSession()) {
            client.addNotification(message);
        }

        actionsHistory.add("A message was sent to everyone registered for session "
                + session.getSessionType() + " on " + session.getSessionDate() + " : " + message);
    }

    public void notify(String date, String message) {
        if (!isAuthorized()) {
            return;
        }

        try {
            // פירמוט התאריך
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, inputFormatter);
            String formattedDate = parsedDate.format(outputFormatter);

            for (Client client : clients) {
                client.addNotification(message);
            }

            actionsHistory.add("A message was sent to everyone registered for a session on " + formattedDate + " : " + message);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy format.");
        }
    }

    public void notify(String message) {
        if (!isAuthorized()) {
            return;
        }

        for (Client client : clients) {
            client.addNotification(message);
        }

        actionsHistory.add("A message was sent to all gym clients: " + message);
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " | Name: " + getName() + " | Gender: " + getGender() +
                " | Birthday: " + getData() + " | Age: " + getAge() +
                // הצגה של היתרה הנוכחית
                " | Balance: " + getMoneyBalance() +
                " | Salary per Month: " + salary;
    }
}