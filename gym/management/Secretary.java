package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.util.ArrayList;
import java.util.List;

public class Secretary extends Person {

    private List<Client> clients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actionsHistory = new ArrayList<>();
    private static   Secretary currentSecretary = null;

    public Secretary(String name, int wage, Gender gender, String date) {
        super(name, wage, gender, date);
        if (currentSecretary != null) {
            throw new IllegalStateException("Only one secretary is allowed.");
        }
        currentSecretary = this;
    }

    public static Secretary getCurrentSecretary() {
        return currentSecretary;
    }


    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException {
        if (p.getAge() < 18) {
            throw new InvalidAgeException("gym.customers.Client age must be 18 or older", p.getAge());
        }

        for (Client client : clients) {
            if (client.equals(new Client(p.getName(), p.getMoneyBalance(), p.getGender(), p.getData(), new ArrayList<>(), new ArrayList<>()))) {
                throw new DuplicateClientException("gym.customers.Client already registered", client);
            }
        }

        Client newClient = new Client(p.getName(), p.getMoneyBalance(), p.getGender(), p.getData(), new ArrayList<>(), new ArrayList<>());
        clients.add(newClient);
        newClient.addNotification("You have successfully registered Fitness Studio Welcome");
        actionsHistory.add("Registered client: " + p.getName());
        return newClient;
    }



    public void unregisterClient(Client c) throws ClientNotRegisteredException {
        if (!clients.contains(c)) {
            throw new ClientNotRegisteredException("Client " + c.getName() + " is not registered in the system.", c);
        }
        clients.remove(c);
        c.addNotification("You have been unregistered from the gym.");
        System.out.println(c.getName() + " unregistered successfully.");
        actionsHistory.add("Unregistered client: " + c.getName());
    }

    public Instructor hireInstructor(Person p, int moneyBalance, List<SessionType> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            throw new IllegalArgumentException("Instructor must be qualified for at least one session.");
        }
        Instructor newInstructor = new Instructor(p.getName(), moneyBalance, p.getGender(), p.getData(), sessions);
        instructors.add(newInstructor);
        actionsHistory.add("Hired instructor: " + p.getName());
        return newInstructor; // לבדוק אולי להוסיף בדיקה אם המדריך קיים כבר
    }


    public Session addSession(SessionType sessionType, String data, ForumType forumType, Instructor instructor) throws InstructorNotQualifiedException {
        try {
            if (!instructor.mayTeach(sessionType)){
                throw new InstructorNotQualifiedException("Instructor is not qualified for " + sessionType, instructor, sessionType);
            }
        }catch (InstructorNotQualifiedException e){
            System.out.println(e.getMessage());
            return null;
        }
        Session newSession = new Session(sessionType, data, forumType, instructor);
        sessions.add(newSession);
        actionsHistory.add("Added session: " + sessionType + " by instructor " + instructor.getName());
        return newSession;
    }

    public boolean registerClientToLesson(Client c, Session session) {
        try {
            if (!clients.contains(c)) {
                throw new ClientNotRegisteredException("Client " + c.getName() + " is not registered in the system.", c);
            }
            if (session.getLientsInSession().contains(c)) {
                throw new DuplicateClientException("Client " + c.getName() + " is already registered for this session.", c);
            }
            if (session.isFull()) {
                throw new IllegalStateException("The session " + session.getSessionType() + " is already full.");
            }
            int sessionPrice = session.getPrice();
            if (c.getMoneyBalance() < sessionPrice) {
                throw new IllegalArgumentException("Client " + c.getName() + " does not have enough balance. Required: " + sessionPrice + ", Available: " + c.getMoneyBalance());
            }
            session.addClient(c);
            c.deductMoney(sessionPrice);
            c.addSessionClient(session);

            System.out.println("Client " + c.getName() + " successfully registered to " + session.getSessionType() + ".");
            actionsHistory.add("Registered client " + c.getName() + " to session: " + session.getSessionType());
            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public int paySalaries() {
        int totalPayment = 0;
        for (Instructor instructor : instructors) {
            int salary = calculateSalary(instructor);
            totalPayment += salary;
        }
        actionsHistory.add("Paid salaries: " + totalPayment);
        return totalPayment;
    }

    public int calculateSalary(Instructor instructor) {
        int salary = 0;
        int numOfSessions = instructor.getInstructorSessions().size();
        int sessionPayment = instructor.getMoneyBalance();

        if (numOfSessions > 0) {
            salary = numOfSessions * sessionPayment;
        }
        return salary;
    }

    public void notify(Session session, String message) {
        System.out.println("Notification for session " + session.getSessionType() + ": " + message);

        for (Session s : sessions) {
            if (s == session) {
                for (Client client : s.getLientsInSession()) {
                    client.addNotification("Notification for session " + session.getSessionType() + ": " + message);
                }
            }
        }
    }
    public void printActions() {
        for (String action : actionsHistory) {
            System.out.println(action);
        }
    }

    public void notify(String date, String message) {
        System.out.println("Notification for date " + date + ": " + message);
        for (Client client : clients) {
            client.addNotification("Notification for date " + date + ": " + message);
        }
    }

    public void notify(String message) {
        System.out.println("General Notification: " + message);

        for (Client client : clients) {
            client.addNotification("General notification: " + message);
        }
    }

}
