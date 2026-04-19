package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("BEVERAGE")
public class Beverage {
    @Id
    private Long id;
    private String name;
    private double price;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String  date;

    public Beverage() {
        // Default constructor for Jackson
    }

    public Beverage(String name, long id, double price, String description) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
