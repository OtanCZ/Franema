package otan.franema.providers;
import otan.franema.entities.CinemaEntity;
import otan.franema.entities.TicketEntity;
import otan.franema.entities.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppProvider {
    //Here are my DB details, I hope you won't do anything bad with them. You can look at the DB if you want, if anyone is stupid enough to put their actual password in the register page you might be able to get their accs.
    static final String DB_URL = "jdbc:mysql://u9_w1ul5NXeCp:Y7l.ir4Ii8C8Wg8EzZvxM1i%3D@node1.otan.cz:3306/s9_Franema";
    static UserEntity currentUser;
    static List<TicketEntity> userTickets;
    static List<TicketEntity> allTickets;
    static List<CinemaEntity> allCinemas;
    static CinemaEntity currentCinema;
    static TicketEntity currentTicket;

    public AppProvider() {
        userTickets = new ArrayList<>();
        allTickets = new ArrayList<>();
        allCinemas = new ArrayList<>();
        fetchAllCinemas();
    }

    public void loginAccount(String username, String password) throws ClassNotFoundException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = \"" + username + "\"");
            if (rs.next()) {
                //This is very stupid, but idc
                UserEntity user = new UserEntity(rs.getString("username"), rs.getString("password"), rs.getBoolean("isAdmin"));
                if(user.getPassword().equals(password)) {
                    currentUser = user;
                }
            } else {
                stmt.executeUpdate("INSERT INTO user VALUES (null, \"" + username + "\", \"" + password + "\", false)");
                currentUser = new UserEntity(username, password, false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOut() {
        System.out.println("User " + currentUser.getUsername() + " logged out.");
        currentUser = null;
    }

    public void fetchAllCinemas() {
        allCinemas.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cinema");
            while (rs.next()) {
                CinemaEntity cinema = new CinemaEntity(rs.getInt("id"), rs.getString("name"), rs.getString("address"));
                allCinemas.add(cinema);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CinemaEntity findCinemaById(int id) {
        for (CinemaEntity cinema : allCinemas) {
            if(cinema.getId() == id) {
                return cinema;
            }
        }
        return null;
    }

    public void saveCinema(CinemaEntity currentCinema) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            if(currentCinema.getId() != 0) {
                stmt.executeUpdate("UPDATE cinema SET name = \"" + currentCinema.getName() + "\", address = \"" + currentCinema.getAddress() + "\" WHERE id = " + currentCinema.getId());
            } else {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO cinema VALUES (null, \"" + currentCinema.getName() + "\", \"" + currentCinema.getAddress() + "\")", Statement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                currentCinema.setId(rs.getInt(1));
                allCinemas.add(currentCinema);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchAvailableTickets() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ticket");
            while (rs.next()) {
                TicketEntity ticket = new TicketEntity(findCinemaById(rs.getInt("cinema_id")), rs.getString("movie"), rs.getString("date"));
                allTickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserEntity getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserEntity currentUser) {
        AppProvider.currentUser = currentUser;
    }

    public static List<TicketEntity> getUserTickets() {
        return userTickets;
    }

    public static void setUserTickets(List<TicketEntity> userTickets) {
        AppProvider.userTickets = userTickets;
    }

    public static List<TicketEntity> getAllTickets() {
        return allTickets;
    }

    public static void setAllTickets(List<TicketEntity> allTickets) {
        AppProvider.allTickets = allTickets;
    }

    public static List<CinemaEntity> getAllCinemas() {
        return allCinemas;
    }

    public static void setAllCinemas(List<CinemaEntity> allCinemas) {
        AppProvider.allCinemas = allCinemas;
    }

    public static CinemaEntity getCurrentCinema() {
        return currentCinema;
    }

    public static void setCurrentCinema(CinemaEntity currentCinema) {
        AppProvider.currentCinema = currentCinema;
    }

    public static TicketEntity getCurrentTicket() {
        return currentTicket;
    }

    public static void setCurrentTicket(TicketEntity currentTicket) {
        AppProvider.currentTicket = currentTicket;
    }
}
