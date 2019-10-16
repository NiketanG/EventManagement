import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.*;
/*TODO
 Generate unique receipt numbers 
 Show receipt
 Payment ?
 View registrations (only done by ADMIN)
 GUI
*/
//set this to 1, or pass 'DEBUG' as command line arguement to enable debug messages
//javac EventManagement.java
//java EventManagement DEBUG

public class EventManagement {
    static int DEBUG = 0;
    static int event_ID;
    static int event_count = 0;
    StringBuilder event_list = new StringBuilder();
    String[] lines;
    void create_event_file() {
        try {
            for (int i = 0; i < event_count; i++) {
                // Creating an object of a file
                File myObj = new File("Event" + (i + 1) + "_registration.dat");
                if (DEBUG == 1) {
                    if (!(myObj.createNewFile())) {
                        System.out.println("File : Event" + (i + 1) + "_registration.dat already exists.");
                    }
                }
            }
            if (DEBUG == 1) {
                System.out.println("/*All Event Registration files created/*");
            }
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
                event_list.append("//");
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

    void show_receipt() {

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
            lines = event_list.toString().split("//");
            int line_count = 0;
            for (int i=0; i<((event_count)); i++) {
                delayed_println("\t\t\t" + ++line_count + "." + lines[i++]);
            }
            event_selected = S.nextInt();
        }
        clearConsole();
        return event_selected;
    }

    void event_details(int event_selected){
        String[] event_info = lines.toString().split("\n");
        System.out.println(event_info);
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

    void drawline() {
        int w = 60;
        for (int i = 0; i < w; i++) {
            System.out.print("_");
        }
    }

    void loading() {
        String anim = "|/-\\";
        System.out.println("\n\n\n");
        for (int x = 0; x < 10; x++) {
            String data = "\r\t\t\tL O A D I N G  " + anim.charAt(x % anim.length()) + " ";
            try {
                System.out.write(data.getBytes());
                Thread.sleep(100);
            } catch (InterruptedException e) {
            } catch (IOException e) {
            }
        }
        clearConsole();
    }

    void typewriter(String text) {
        String text_to_display = text;
        for (int i = 0; i <= text_to_display.length(); i++) {
            System.out.print("\r" + text_to_display.substring(0, i));
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
            }
        }
    }

    void delayed_println(String text) {
        System.out.println(text);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {}
    }

    void intro() {
        drawline();
        System.out.print("\n\n\n");
        typewriter("\t\t\tW E L C O M E\n");
        typewriter("\t\t\t_____________");
        System.out.println();
        delayed_println("\n\t\tGovernment Polytechnic, Pune");
        delayed_println("\t\t\t   Events");
        event_ID = select_Event();
        event_details(event_ID);
        System.out.println("\n\n\n");
    }

    public static void main(String[] args) {
        try {
            if ((args[0].equals("DEBUG")) || (args[0].equals("debug"))) {
                DEBUG = 1;
                System.out.println("*/DEBUG MODE ON/*");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        EventManagement E1 = new EventManagement();
        E1.loading();
        E1.intro();
        //E1.enterDetails();
    }
}