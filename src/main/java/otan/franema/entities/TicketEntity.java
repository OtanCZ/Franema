package otan.franema.entities;

public class TicketEntity {
    private CinemaEntity cinema;
    private String movie;
    private String date;

    public TicketEntity(CinemaEntity cinema, String movie, String date) {
        this.cinema = cinema;
        this.movie = movie;
        this.date = date;
    }

    public CinemaEntity getCinema() {
        return cinema;
    }

    public void setCinema(CinemaEntity cinema) {
        this.cinema = cinema;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "cinema=" + cinema +
                ", movie='" + movie + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
