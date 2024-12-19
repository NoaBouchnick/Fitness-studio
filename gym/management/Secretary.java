package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.List;

public class Secretary extends Person {

    private List<Client> clients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actionsHistory = new ArrayList<>();
    private static   Secretary currentSecretary = null;
    private boolean isActive = true;

    public Secretary(String name, int wage, Gender gender, String date) {
        super(name, wage, gender, date);
        if (currentSecretary != null) {
            throw new IllegalStateException("Only one secretary is allowed.");
        }
        currentSecretary = this;
        actionsHistory.add("A new secretary has started working at the gym: " + getName());
    }
    public void deactivate() {
        isActive = false;
    }

    private boolean isAuthorized() {
        return isActive && Secretary.getCurrentSecretary() == this;
    }

    public static Secretary getCurrentSecretary() {
        return Gym.getInstance().getSecretary();
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException {
        try {
            if (!isAuthorized()) {
                System.out.println("Unauthorized action: This secretary is no longer active.");
                return null;
            }
            if (p.getAge() < 18) {
                throw new InvalidAgeException("Client age must be 18 or older", p.getAge());
            }
            for (Client client : clients) {
                if (client.equals(new Client(p.getName(), p.getMoneyBalance(), p.getGender(), p.getData(), new ArrayList<>(), new ArrayList<>()))) {
                    throw new DuplicateClientException("Client already registered", client);
                }
            }
            Client newClient = new Client(p.getName(), p.getMoneyBalance(), p.getGender(), p.getData(), new ArrayList<>(), new ArrayList<>());
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
        try {
            if (!isAuthorized()) {
                System.out.println("Unauthorized action: This secretary is no longer active.");
                return;
            }
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
        try {
            if (!isAuthorized()) {
                return null;
            }
            if (sessions == null || sessions.isEmpty()) {
                throw new IllegalArgumentException("Instructor must be qualified for at least one session.");
            }
            Instructor newInstructor = new Instructor(p.getName(), p.getMoneyBalance(), p.getGender(), p.getData(), sessions, hourlyWage);
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
        try {
            if (!isAuthorized()) {
                return null;
            }
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
        try {
            if (!isAuthorized()) {
                return false;
            }
            if (!clients.contains(c)) {
                throw new ClientNotRegisteredException("The client is not registered with the gym and cannot enroll in lessons", c);
            }
            if (session.getClientsInSession().contains(c)) {
                throw new DuplicateClientException("The client is already registered for this lesson", c);
            }
            if (session.getSessionDate().isBefore(LocalDateTime.now())) {
                actionsHistory.add("Failed registration: Session is not in the future");
                return false;
            }
            int sessionPrice = session.getPrice();
            if (c.getMoneyBalance() < sessionPrice) {
                actionsHistory.add("Failed registration: Client doesn't have enough balance");
                throw new IllegalArgumentException("Client " + c.getName() + " does not have enough balance. Required: " + sessionPrice + ", Available: " + c.getMoneyBalance());
            }
            for (Instructor instructor : instructors) {
                if (instructor.getName().equals(c.getName()) && instructor.isTeachingAtTime(session.getSessionDate())) {
                    return false;
                }
            }
            if (session.isFull()) {
                actionsHistory.add("Failed registration: No available spots for session");
                throw new IllegalStateException("The session " + session.getSessionType() + " is already full.");
            }
            ForumType forumType = session.getForumType();
            switch (forumType) {
                case All:
                    break;
                case Seniors:
                    if (c.getAge() < 65) {
                        actionsHistory.add("Failed registration: Client doesn't meet the age requirements for this session (Seniors)");
                        return false;
                    }
                    break;
                case Male:
                    if (c.getGender() != Gender.Male) {
                        actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements (expected Male).");
                        return false;
                    }
                    break;
                case Female:
                    if (c.getGender() != Gender.Female) {
                        actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements (expected Female).");
                        return false;
                    }
                    break;
            }
            session.addClient(c);
            c.deductMoney(sessionPrice);
            c.addSessionClient(session);

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
//        ensureActiveSecretary();
        int totalPayment = 0;

        for (Instructor instructor : instructors) {
            int numOfSessions = instructor.getSessionsTaught().size();
            int hourlyWage = instructor.getHourlyWage();

            int salary = numOfSessions * hourlyWage;
            instructor.setMoneyBalance(instructor.getMoneyBalance() + salary);

            totalPayment += salary;
        }

        actionsHistory.add("Total salaries paid: " + totalPayment);
        return totalPayment;
    }

    public void printActions() {
        for (String action : actionsHistory) {
            System.out.println(action);
        }
    }

    public void notify(Session session, String message) {
        String notification = "Notification for session " + session.getSessionType() + ": " + message;
        for (Client client : session.getClientsInSession()) {
            client.addNotification(notification);
        }
        actionsHistory.add("A message was sent to everyone registered for session " + session.getSessionType() + " on " + session.getSessionDate() + ": " + message);
    }

    public void notify(String date, String message) {
        String notification = "Notification for date " + date + ": " + message;
        for (Client client : clients) {
            client.addNotification(notification + "\n");
        }
        actionsHistory.add("Notification sent for date " + date + ": " + message + "\n");
    }

    public void notify(String message) {
        for (Client client : clients) {
            client.addNotification("General notification: " + message + "\n");
        }
        actionsHistory.add("General notification sent to all clients: " + message + "\n");
    }
}