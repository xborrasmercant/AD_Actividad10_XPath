import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class BookingManager {
    File bookingsFile;
    ArrayList<Booking> bookingsCollection = new ArrayList<>();


    public static void main(String[] args) {
        BookingManager bm = new BookingManager();
        // Initialization of objects
        Scanner input = new Scanner(System.in);
        String i;

        // Main menu
        while (true) {
            System.out.println();
            System.out.println("===============================");
            System.out.println("1. Show existing bookings");
            System.out.println("2. Generate IDs XML");
            System.out.println("3. Exit");
            System.out.println("===============================");

            System.out.print("Enter an option please (1-3): ");
            i = input.nextLine();
            System.out.println("-------------------------------");

            switch (i) {
                case "1":
                    bm.loadBookingsFile();
                    bm.printAllBookings();
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


    public void printAllBookings() {
        for (Booking booking : bookingsCollection) {
            booking.printBooking();
        }
    }

    public void extractBookings(Document doc) {
        Booking booking;
        XPathExpression bookingsPath;
        Element bookingElement;

        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath myXPath = factory.newXPath();
            XPathExpression count = myXPath.compile("count(bookings/booking)");
            int bookingsTotal = ((Number) count.evaluate(doc, XPathConstants.NUMBER)).intValue();

            for (int locNum = 1; locNum <= bookingsTotal; locNum++) {
                bookingsPath = myXPath.compile("/bookings/booking[@location_number='" + locNum + "']");
                bookingElement = (Element) bookingsPath.evaluate(doc, XPathConstants.NODE);

                // Fill booking and add to Booking ArrayList
                booking = fillBookingAttributes(bookingElement);
                bookingsCollection.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadBookingsFile() {
        File bookingsFile = new File("src/bookings.xml");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(bookingsFile);

            extractBookings(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Booking fillBookingAttributes(Element bookingElement){
        Booking booking = new Booking();

        // Booking
        booking.setBookingID(bookingElement.getAttribute("location_number"));

        // Client
        booking.setClientID(((Element) bookingElement.getElementsByTagName("client").item(0)).getAttribute("id_client"));
        booking.setClientName(bookingElement.getElementsByTagName("client").item(0).getTextContent());

        // Agency
        booking.setAgencyID(((Element) bookingElement.getElementsByTagName("agency").item(0)).getAttribute("id_agency"));
        booking.setAgencyName(bookingElement.getElementsByTagName("agency").item(0).getTextContent());

        // Price
        booking.setPrice(bookingElement.getElementsByTagName("price").item(0).getTextContent());

        // Room
        booking.setRoomID(((Element) bookingElement.getElementsByTagName("room").item(0)).getAttribute("id_type"));
        // Room string is automatically added.

        // Hotel
        booking.setHotelID(((Element) bookingElement.getElementsByTagName("hotel").item(0)).getAttribute("id_hotel"));
        booking.setHotelName(bookingElement.getElementsByTagName("hotel").item(0).getTextContent());

        // Check In
        booking.setCheckIn(bookingElement.getElementsByTagName("check_in").item(0).getTextContent());

        // Nights
        booking.setRoomNights(bookingElement.getElementsByTagName("room_nights").item(0).getTextContent());

        return booking;
    }

}
