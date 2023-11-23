import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class clientManager {
    File bookingsFile;
    ArrayList<Booking> bookingsCollection = new ArrayList<>();


    public static void main(String[] args) {
        // Initialization of objects
        Scanner input = new Scanner(System.in);
        String i;

        // Main menu
        while (true) {
            System.out.println();
            System.out.println("===============================");
            System.out.println("1. Show existing booking");
            System.out.println("2. Generate IDs XML");
            System.out.println("3. Exit");
            System.out.println("===============================");

            System.out.print("Enter an option please (1-3): ");
            i = input.nextLine();
            System.out.println("-------------------------------");

            switch (i) {
                case "1":
                    loadBookingsFile()
                    System.out.println("Show XML File");
                    break;
                case "2":
                    System.out.println("Generate XML File");
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please, try again.");
                    break;
            }
        }
    }

    public void extractBookings(Document doc) {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath myXPath = factory.newXPath();
            XPathExpression count = myXPath.compile("count(bookings/booking)");
            int bookingsTotal = (int) count.evaluate(doc, XPathConstants.NUMBER);

            for (int locNum = 0; locNum <= bookingsTotal; locNum++) {
                XPathExpression bookings = myXPath.compile("/bookings/booking[@location_number='" + locNum + "']");
                Element bookingElement = (Element) bookings.evaluate(doc, XPathConstants.NODE);

                // Print values
                System.out.println("Location number : " + bookingElement.getAttribute("location_number"));
                System.out.println("Client : " + bookingElement.getElementsByTagName("client").item(0).getTextContent());
                System.out.println("Agency : " + bookingElement.getElementsByTagName("agency").item(0).getTextContent());
                System.out.println("Price : " + bookingElement.getElementsByTagName("price").item(0).getTextContent());
                System.out.println("Hotel : " + bookingElement.getElementsByTagName("hotel").item(0).getTextContent());

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadBookingsFile() {
        File bookingsFile = new File("src/bookings.xml");

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(bookingsFile);

            extractBookings(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }    }


}
