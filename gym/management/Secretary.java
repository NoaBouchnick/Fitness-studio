package gym.management;

import gym.Exception.ClientNotRegisteredException;
import gym.Exception.DuplicateClientException;
import gym.Exception.InstructorNotQualifiedException;
import gym.Exception.InvalidAgeException;
import gym.customers.Person;
import gym.customers.Client;
import gym.customers.Gender;
import gym.management.Sessions.SessionType;
import gym.management.Sessions.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Secretary extends Person {

    private HashMap<Client, List<Session>> register;
    private List<Instructor> instructors;
    private List<String> actionsHistory;

    public Secretary(String name, int financialBalance, Gender gender, String date) {
        super(name, financialBalance, gender, date);
        this.register = new HashMap<>();
        this.instructors = new ArrayList<>();
        this.actionsHistory = new ArrayList<>();
    }


    public void notify(Session session, String message) {
        System.out.println("Notification for session " + session.getSessionType() + ": " + message);
        addActionsHistory("Notified session: " + session.getSessionType());

        for (Client client : register.keySet()) {
            List<Session> sessions = register.get(client);
            if (sessions != null && sessions.contains(session)) {
                client.addNotification("Notification for session: " + session.getSessionType() + ". Message: " + message);
            }
        }
    }


    public void notify(String date, String message) {
        System.out.println("Notification for date " + date + ": " + message);
        addActionsHistory("Notified date: " + date);
    }

    public void notify(String message) {
        System.out.println("General Notification: " + message);
        addActionsHistory("General notification sent");
        for (Client client : register.keySet()) {
            client.addNotification("General notification: " + message);
        }
    }


    public void notifyClientsAboutSubscription() {
        for (Client client : register.keySet()) {
            client.addNotification("Your subscription payment has been processed. Thank you for staying with us!");
            System.out.println("Notified " + client.getName() + " about subscription payment.");
        }
        addActionsHistory("Notified all clients about subscription payments.");
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException {
        if (p.getAge() < 18) {
            throw new InvalidAgeException("gym.customers.Client age must be 18 or older", p.getAge());
        }

        List<String> notifications = new ArrayList<>();
        Client newClient = new Client(p.getName(), p.getFinancialBalance(), p.getGender(), p.getData(), notifications);

        if (register.containsKey(newClient)) {
            throw new DuplicateClientException("gym.customers.Client already registered", newClient);
        }

        register.put(newClient, new ArrayList<>());
        addActionsHistory("Registered client: " + p.getName());
        System.out.println(p.getName() + " registered successfully");
        return newClient;
    }


    public void unregisterClient(Client c) throws ClientNotRegisteredException {
        if (!register.containsKey(c)) {
            throw new ClientNotRegisteredException("Client " + c.getName() + " is not registered in the system.", c);
        }
        register.remove(c);
        c.addNotification("You have been unregistered from the gym.");
        System.out.println(c.getName() + " unregistered successfully.");

    }

    public Instructor hireInstructor(Person p, int balance, ArrayList<SessionType> session) {
        Instructor newInstructor = new Instructor(p.getName(), balance, p.getGender(), p.getData(), session);
        instructors.add(newInstructor);
        return newInstructor;
    }

    public Session addSession(SessionType sessionType, String dueData, ForumType forumType, Instructor instructor) throws InstructorNotQualifiedException {
        int defaultCapacity = 20; // קיבולת ברירת מחדל
        return addSession(sessionType, dueData, forumType, instructor, defaultCapacity);
    }

    public Session addSession(SessionType sessionType, String dueData, ForumType forumType, Instructor instructor, int maxCapacity) throws InstructorNotQualifiedException {
        if (!instructor.isQualifiedForSession(sessionType)) {
            throw new InstructorNotQualifiedException("Instructor is not qualified for " + sessionType, instructor, sessionType);
        }
        Session session = new Session(sessionType, dueData, forumType, instructor, maxCapacity);
        System.out.println("Session for " + sessionType + " added successfully!");
        return session;
    }

    public boolean registerClientToLesson(Client c, Session session) throws ClientNotRegisteredException {
        if (!register.containsKey(c)) {
            throw new ClientNotRegisteredException("Client " + c.getName() + " is not registered in the system.", c);
        }
        List<Session> clientSessions = register.get(c);
        if (clientSessions == null) {
            clientSessions = new ArrayList<>();
            register.put(c, clientSessions);
        }
        if (clientSessions.contains(session)) {
            addActionsHistory("Failed to register client " + c.getName() + " to session: " + session.getSessionType() + " - already registered.");
            System.out.println("Client " + c.getName() + " is already registered to session: " + session.getSessionType());
            return false;
        }
        clientSessions.add(session);
        addActionsHistory("Successfully registered client " + c.getName() + " to session: " + session.getSessionType());
        System.out.println(c.getName() + " registered successfully to session: " + session.getSessionType());
        return true;
    }

    public int paySalaries() {
        int totalPayment = 0;
        for (Instructor instructor : instructors) {
            int salary = calculateSalary(instructor);
            totalPayment += salary;
            addActionsHistory("Paid salary to instructor: " + instructor.getName());
        }
        return totalPayment;
    }

    public int calculateSalary(Instructor instructor) {
        int salary = 0;
        int numOfSessions = instructor.getSessions().size();
        int sessionPayment = 1000;
        salary += numOfSessions * sessionPayment;
        return salary;
    }

    private void addActionsHistory(String action) {
        if (actionsHistory == null) {
            actionsHistory = new ArrayList<>();
        }
        actionsHistory.add(action);
        System.out.println("Action added to history: " + action);
    }

    public void printActions() {
        if (actionsHistory == null || actionsHistory.isEmpty()) {
            System.out.println("No actions have been recorded yet.");
        } else {
            for (String action : actionsHistory) {
                System.out.println(action);
            }
        }
    }

    public void cancelSession(Session session) {
        if (actionsHistory == null) actionsHistory = new ArrayList<>(); // Ensure actionsHistory is initialized
        for (Client client : register.keySet()) {
            List<Session> clientSessions = register.get(client);
            if (clientSessions != null && clientSessions.contains(session)) {
                clientSessions.remove(session);
                client.addNotification("The session " + session.getSessionType() + " scheduled for " + session.getDueData() + " has been canceled.");
            }
        }
        System.out.println("Session " + session.getSessionType() + " has been canceled.");
        addActionsHistory("Canceled session: " + session.getSessionType());
    }

    public void renewSubscription(Client client) {
        if (actionsHistory == null) actionsHistory = new ArrayList<>(); // Ensure actionsHistory is initialized
        if (register.containsKey(client)) {
            client.addNotification("Your subscription has been successfully renewed. Thank you for staying with us!");
            System.out.println("Subscription renewed for client: " + client.getName());
            addActionsHistory("Subscription renewed for client: " + client.getName());
        } else {
            System.out.println("Client " + client.getName() + " is not registered.");
        }
    }

    public void changeInstructor(Session session, Instructor newInstructor) throws InstructorNotQualifiedException {
        if (actionsHistory == null) actionsHistory = new ArrayList<>(); // Ensure actionsHistory is initialized
        if (!newInstructor.isQualifiedForSession(session.getSessionType())) {
            throw new InstructorNotQualifiedException("Instructor " + newInstructor.getName() + " is not qualified for " + session.getSessionType(), newInstructor, session.getSessionType());
        }

        Instructor oldInstructor = session.getInstructor();
        session.setInstructor(newInstructor);

        for (Client client : register.keySet()) {
            List<Session> clientSessions = register.get(client);
            if (clientSessions != null && clientSessions.contains(session)) {
                client.addNotification("The instructor for " + session.getSessionType() + " has been changed from " + oldInstructor.getName() + " to " + newInstructor.getName());
            }
        }

        System.out.println("Instructor for session " + session.getSessionType() + " changed to " + newInstructor.getName());
        addActionsHistory("Instructor for session " + session.getSessionType() + " changed.");
    }

    public void notifySubscriptionPayments() {
        if (actionsHistory == null) actionsHistory = new ArrayList<>(); // Ensure actionsHistory is initialized
        for (Client client : register.keySet()) {
            client.addNotification("Your subscription payment has been processed. Thank you for staying with us!");
            System.out.println("Notified " + client.getName() + " about subscription payment.");
            addActionsHistory("Notified " + client.getName() + " about subscription payment.");
        }
    }
    public void printClientStatus(Client client) {
        if (actionsHistory == null) actionsHistory = new ArrayList<>(); // Ensure actionsHistory is initialized
        System.out.println("Client: " + client.getName());
        System.out.println("Notifications: " + client.getNotifications());
        System.out.println("Registered Sessions: " + register.get(client));
    }
}
