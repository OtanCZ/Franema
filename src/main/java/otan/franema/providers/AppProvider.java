package otan.franema.providers;

import otan.franema.entities.CinemaEntity;
import otan.franema.entities.TicketEntity;
import otan.franema.entities.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppProvider {
    //Here are my DB details, I hope you won't do anything bad with them. You can look at the DB if you want, if anyone is stupid enough to put their actual password in the register page you might be able to get their accs.
    private final String DB_URL = "jdbc:mysql://u9_w1ul5NXeCp:Y7l.ir4Ii8C8Wg8EzZvxM1i%3D@node1.otan.cz:3306/s9_Franema";
    private UserEntity currentUser;
    private List<TicketEntity> userTickets;
    private List<TicketEntity> allTickets;
    private List<CinemaEntity> allCinemas;
    private CinemaEntity currentCinema;
    private TicketEntity currentTicket;

    public AppProvider() {
        userTickets = new ArrayList<>();
        allTickets = new ArrayList<>();
        allCinemas = new ArrayList<>();
        fetchAllCinemas();
        fetchAvailableTickets();
    }

    public void loginAccount(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = \"" + username + "\"");
            if (rs.next()) {
                //This is very stupid, but idc
                UserEntity user = new UserEntity(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getBoolean("isAdmin"));
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    fetchUserTickets();
                }
            } else {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO user VALUES (null, ?, ?, false)", Statement.RETURN_GENERATED_KEYS);
                stmt2.setString(1, username);
                stmt2.setString(2, password);
                stmt2.executeUpdate();
                ResultSet rs2 = stmt2.getGeneratedKeys();
                if (rs2.next()) {
                    currentUser = new UserEntity(rs2.getInt(1), username, password, false);
                }
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
            if (cinema.getId() == id) {
                return cinema;
            }
        }
        return null;
    }

    public void saveCinema(CinemaEntity currentCinema) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            if (currentCinema.getId() != 0) {
                stmt.executeUpdate("UPDATE cinema SET name = \"" + currentCinema.getName() + "\", address = \"" + currentCinema.getAddress() + "\" WHERE id = " + currentCinema.getId());
            } else {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO cinema VALUES (null, \"" + currentCinema.getName() + "\", \"" + currentCinema.getAddress() + "\")", Statement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    currentCinema.setId(rs.getInt(1));
                    allCinemas.add(currentCinema);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCinema(CinemaEntity cinema) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM cinema WHERE id = " + cinema.getId());
            allCinemas.remove(cinema);
            for (TicketEntity ticket : allTickets) {
                if (ticket.getCinema().getId() == cinema.getId()) {
                    allTickets.remove(ticket);
                }
            }

            for (TicketEntity ticket : userTickets) {
                if (ticket.getCinema().getId() == cinema.getId()) {
                    userTickets.remove(ticket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchAvailableTickets() {
        allTickets.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ticket");
            while (rs.next()) {
                TicketEntity ticket = new TicketEntity(rs.getInt("id"), findCinemaById(rs.getInt("cinema_id")), rs.getString("movie"), rs.getString("time"));
                allTickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchUserTickets() {
        userTickets.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ticket_user WHERE user_id = " + currentUser.getId());
            while (rs.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM ticket WHERE id = ?");
                stmt2.setInt(1, rs.getInt("ticket_id"));
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    TicketEntity ticket = new TicketEntity(rs2.getInt("id"), findCinemaById(rs2.getInt("cinema_id")), rs2.getString("movie"), rs2.getString("time"));
                    userTickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TicketEntity findTicketById(int id) {
        for (TicketEntity ticket : allTickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

    public void saveTicket(TicketEntity currentTicket) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            if (currentTicket.getId() != 0) {
                stmt.executeUpdate("UPDATE ticket SET cinema_id = " + currentTicket.getCinema().getId() + ", movie = \"" + currentTicket.getMovie() + "\", time = \"" + currentTicket.getDate() + "\" WHERE id = " + currentTicket.getId());
            } else {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO ticket VALUES (null, " + currentTicket.getCinema().getId() + ", \"" + currentTicket.getMovie() + "\", \"" + currentTicket.getDate() + "\")", Statement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    currentTicket.setId(rs.getInt(1));
                    allTickets.add(currentTicket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTicket(TicketEntity ticket) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM ticket WHERE id = " + ticket.getId());
            allTickets.remove(ticket);
            for (TicketEntity userTicket : userTickets) {
                if (userTicket.getId() == ticket.getId()) {
                    userTickets.remove(userTicket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void buyTicket(TicketEntity ticket) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO ticket_user VALUES (" + ticket.getId() + ", " + currentUser.getId() + ")");
            userTickets.add(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<TicketEntity> getUserTickets() {
        return userTickets;
    }

    public void setUserTickets(List<TicketEntity> userTickets) {
        this.userTickets = userTickets;
    }

    public List<TicketEntity> getAllTickets() {
        return allTickets;
    }

    public void setAllTickets(List<TicketEntity> allTickets) {
        this.allTickets = allTickets;
    }

    public List<CinemaEntity> getAllCinemas() {
        return allCinemas;
    }

    public void setAllCinemas(List<CinemaEntity> allCinemas) {
        this.allCinemas = allCinemas;
    }

    public CinemaEntity getCurrentCinema() {
        return currentCinema;
    }

    public void setCurrentCinema(CinemaEntity currentCinema) {
        this.currentCinema = currentCinema;
    }

    public TicketEntity getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(TicketEntity currentTicket) {
        this.currentTicket = currentTicket;
    }
}
