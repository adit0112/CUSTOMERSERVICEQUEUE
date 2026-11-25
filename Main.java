import java.util.*;
import java.io.*;

// Customer Class
class Customer {
    String token;
    String name;
    String serviceType;

    Customer(String token, String name, String serviceType) {
        this.token = token;
        this.name = name;
        this.serviceType = serviceType;
    }

    public String toString() {
        return token + " | " + name + " | " + serviceType;
    }
}

// Queue Manager Class
class QueueManager {
    Queue<Customer> queue = new LinkedList<>();
    HashMap<String, Customer> customerMap = new HashMap<>();
    int tokenNumber = 101;

    void addCustomer(String name, String service) {
        String token = "C" + tokenNumber++;

        Customer c = new Customer(token, name, service);

        queue.add(c);
        customerMap.put(token, c);

        System.out.println("\nCustomer Added Successfully!");
        System.out.println("Token Number: " + token);
    }

    void serveCustomer() {
        if (queue.isEmpty()) {
            System.out.println("\nQueue is Empty!");
            return;
        }

        Customer c = queue.poll();
        customerMap.remove(c.token);

        System.out.println("\nNow Serving:");
        System.out.println(c);
        System.out.println("Customer Served Successfully!");
    }

    void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("\nNo Waiting Customers!");
            return;
        }

        System.out.println("\n--- WAITING CUSTOMER QUEUE ---");

        for (Customer c : queue) {
            System.out.println(c);
        }
    }

    void searchCustomer(String token) {
        Customer c = customerMap.get(token);

        if (c != null) {
            System.out.println("\nCustomer Found:");
            System.out.println(c);
        } else {
            System.out.println("\nCustomer Not Found!");
        }
    }

    void queueStatus() {
        System.out.println("\nTotal Waiting Customers: " + queue.size());

        if (!queue.isEmpty()) {
            System.out.println("Next Customer: " + queue.peek());
        } else {
            System.out.println("Queue is Empty!");
        }
    }
}

// File Manager Class
class FileManager {
    static final String FILE = "customers.txt";

    static void save(QueueManager qm) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Customer c : qm.queue) {
                pw.println(c.token + "," + c.name + "," + c.serviceType);
            }
            System.out.println("Data Saved Successfully!");
        } catch (IOException e) {
            System.out.println("File Saving Error!");
        }
    }

    static void load(QueueManager qm) {
        File file = new File(FILE);

        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 3);

                if (data.length == 3) {
                    Customer c = new Customer(
                        data[0], data[1], data[2]
                    );

                    qm.queue.add(c);
                    qm.customerMap.put(c.token, c);

                    int num = Integer.parseInt(data[0].substring(1));
                    qm.tokenNumber = Math.max(qm.tokenNumber, num + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("File Loading Error!");
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        QueueManager qm = new QueueManager();

        FileManager.load(qm);

        int choice=0;

        do {
            System.out.println("\n===== CUSTOMER SERVICE QUEUE SYSTEM =====");
            System.out.println("1. Add New Customer");
            System.out.println("2. Serve Next Customer");
            System.out.println("3. Display Waiting Customers");
            System.out.println("4. Search Customer");
            System.out.println("5. Check Queue Status");
            System.out.println("6. Exit");

            System.out.print("Enter Your Choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid Input!");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter Service Type: ");
                    String service = sc.nextLine().trim();

                    if (name.isEmpty() || service.isEmpty()) {
                        System.out.println("Name and Service cannot be empty!");
                    } else {
                        qm.addCustomer(name, service);
                    }
                    break;

                case 2:
                    qm.serveCustomer();
                    break;

                case 3:
                    qm.displayQueue();
                    break;

                case 4:
                    System.out.print("Enter Token Number: ");
                    String token = sc.nextLine().trim().toUpperCase();
                    qm.searchCustomer(token);
                    break;

                case 5:
                    qm.queueStatus();
                    break;

                case 6:
                    FileManager.save(qm);
                    System.out.println("Thank You! Exiting System...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 6);

        sc.close();
    }
}