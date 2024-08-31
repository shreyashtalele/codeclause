import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class representing a movie
class Movie {
    private String title;
    private int duration; // in minutes

    public Movie(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return title + " (" + duration + " min)";
    }
}

// Class representing a seat in the theater
class Seat {
    private String seatNumber;
    private boolean isReserved;

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void reserve() {
        isReserved = true;
    }

    @Override
    public String toString() {
        return seatNumber + (isReserved ? " (Reserved)" : " (Available)");
    }
}

// Class representing the theater
class Theater {
    private List<Seat> seats;

    public Theater(int numRows, int numCols) {
        seats = new ArrayList<>();
        char row = 'A';
        for (int i = 0; i < numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                seats.add(new Seat(row + String.valueOf(j)));
            }
            row++;
        }
    }

    public void displaySeats() {
        System.out.println("Available Seats:");
        for (Seat seat : seats) {
            System.out.print(seat + " ");
            if (seat.getSeatNumber().endsWith("5")) { // for formatting, adjust as needed
                System.out.println();
            }
        }
        System.out.println();
    }

    public boolean reserveSeat(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                if (!seat.isReserved()) {
                    seat.reserve();
                    return true;
                } else {
                    System.out.println("Seat " + seatNumber + " is already reserved.");
                    return false;
                }
            }
        }
        System.out.println("Seat " + seatNumber + " does not exist.");
        return false;
    }
}

// Main class to run the booking system
public class MovieTicketBookingSystem {

    private static List<Movie> movies = new ArrayList<>();
    private static Theater theater = new Theater(5, 10); // 5 rows, 10 columns

    public static void main(String[] args) {
        // Sample movies
        movies.add(new Movie("The Shawshank Redemption", 142));
        movies.add(new Movie("The Godfather", 175));
        movies.add(new Movie("The Dark Knight", 152));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to Movie Ticket Booking System");
            System.out.println("1. View Movies");
            System.out.println("2. View Available Seats");
            System.out.println("3. Book Ticket");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMovies();
                    break;
                case 2:
                    theater.displaySeats();
                    break;
                case 3:
                    bookTicket(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewMovies() {
        System.out.println("Available Movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }
        System.out.println();
    }

    private static void bookTicket(Scanner scanner) {
        viewMovies();
        System.out.print("Select a movie by number: ");
        int movieIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (movieIndex < 0 || movieIndex >= movies.size()) {
            System.out.println("Invalid movie selection.");
            return;
        }

        Movie selectedMovie = movies.get(movieIndex);
        System.out.println("Selected Movie: " + selectedMovie);
        theater.displaySeats();

        System.out.print("Enter seat number to reserve: ");
        String seatNumber = scanner.nextLine();

        if (theater.reserveSeat(seatNumber)) {
            System.out.println("Ticket booked successfully for " + selectedMovie.getTitle() + " in seat " + seatNumber + ".");
        } else {
            System.out.println("Failed to book the ticket. Please try again.");
        }
        System.out.println();
    }
}
