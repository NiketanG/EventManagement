import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
/*TODO
 Generate unique receipt numbers 
 Show receipt
 Payment ?
 View registrations (only done by ADMIN)
 GUI
*/
public class EventManagement {
    static int event_ID;
    static int event_count = 0;
    StringBuilder event_list = new StringBuilder();

    void create_event_file() {
        try {
            for (int i = 0; i < event_count; i++) {
                // Creating an object of a file
                File myObj = new File("Event" + (i + 1) + "_registration.dat");
                if (!(myObj.createNewFile())) {
                    System.out.println("File already exists.");
                }
            }
            System.out.println("Event Registration files created");
        } catch (IOException e) {
            System.out.println("An error occurred during creating registration files for Event.");
            e.printStackTrace();
        }
    }

    void get_events() {
        try {
            // Creating an object of the file for reading the data
            File myObj = new File("Event_list.dat");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                event_count++;
                String event_name = myReader.nextLine();
                // System.out.println(event_count + "." + event_name);
                event_list.append(event_name);
                event_list.append(",");
            }
            myReader.close();
            create_event_file();
        } catch (FileNotFoundException e) {
            System.out.println("There was an error while fetching the Event List.");
            e.printStackTrace();
        }
    }

    void write_to_file(int event_ID, String name, String email, String phno) {
        try {
            FileWriter myWriter = new FileWriter("Event" + event_ID + "_registration.dat", true);
            // Writes this content into the specified file
            myWriter.write(name + System.getProperty("line.separator") + email + System.getProperty("line.separator")
                    + phno + System.getProperty("line.separator"));

            // Closing is necessary to retrieve the resources allocated
            myWriter.close();
            System.out.println("Registration Successful !");
        } catch (IOException e) {
            System.out.println("An error occurred while saving registration details.");
            e.printStackTrace();
        }
    }

    void show_receipt(){

    }
    public final static void clearConsole() {
        // System.out.println(new String(new char[50]).replace("\0", "\r\n"));
        // Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
        }
    }

    int select_Event() {
        Scanner S = new Scanner(System.in);
        int event_selected = 0;
        while (event_selected == 0) {
            System.out.println("Select the event in which you would like to participate in :");
            // Get event list from file
            get_events();
            // System.out.println("1.Webber\n2.Webber\n3.Clash of Code\n4.Project
            // Presentation\n5.Flash\n6.Capture");
            String[] lines = event_list.toString().split(",");
            int line_count = 0;
            for (String str : lines) {
                System.out.println(++line_count + "." + str);
            }
            event_selected = S.nextInt();
        }
        clearConsole();
        return event_selected;
    }

    void enterDetails() {
        Scanner S = new Scanner(System.in);
        System.out.println("EventID : " + event_ID);
        try {
            String name, email;
            String phno;

            System.out.println("Enter Name : ");
            name = S.nextLine();
            while (name.length() <= 0) {
                System.out.println("*Name cannot be blank");
                name = S.nextLine();
            }

            System.out.println("Enter Email Address : ");
            email = S.nextLine();
            while (email.length() <= 0) {
                System.out.println("*Email cannot be blank");
                email = S.nextLine();
            }

            System.out.println("Enter Phone No. : ");
            phno = S.nextLine();
            while (phno.length() < 10) {
                if (phno.length() == 0)
                    System.out.println("*Phone No. cannot be blank");
                System.out.println("*Phone No. must consist of 10 digits");
                phno = S.nextLine();
            }

            write_to_file(event_ID, name, email, phno);
        } catch (InputMismatchException e) {
            System.out.println("Error - Invalid data type entered. Please enter correct information in proper format.");
            enterDetails();
        }
    }

    public static void main(String[] args) {
        EventManagement E1 = new EventManagement();
        event_ID = E1.select_Event();
        E1.enterDetails();
    }
}