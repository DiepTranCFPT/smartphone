package managesmartphone.diepxdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(name = "date_of_birth")
    private String dob; // dd-mm-yyyy format

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Prevent circular reference in JSON serialization
    private List<Purchase> purchases = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now().plusHours(7);
    }

    // Helper method to get order count
    public int getOrderCount() {
        return purchases != null ? purchases.size() : 0;
    }

    // Helper methods for date handling
    @Transient
    public String getDobDate() {
        if (dob != null && !dob.isEmpty()) {
            try {
                // Convert from dd-mm-yyyy to yyyy-mm-dd for HTML date input
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dob, inputFormatter);
                return date.format(outputFormatter);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    @Transient
    public void setDobDate(String dobDate) {
        if (dobDate != null && !dobDate.isEmpty()) {
            try {
                // Convert from yyyy-mm-dd (HTML date input) to dd-mm-yyyy (database format)
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date = LocalDate.parse(dobDate, inputFormatter);
                this.dob = date.format(outputFormatter);
            } catch (Exception e) {
                this.dob = "";
            }
        }
    }
}
