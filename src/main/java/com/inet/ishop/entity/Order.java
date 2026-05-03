package com.inet.ishop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("customer_name") // ប្រាប់ Jackson ឱ្យចាប់យក Key ពី JSON
    @Column(name = "customer_name") // ប្រាប់ JPA ឱ្យបញ្ជូនទៅ Column ក្នុង DB
    private String customerName;

    @JsonProperty("total_amount")
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "status")
    private String status;
    @JsonProperty("order_date")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    // កូដនេះនឹងដើរអូតូ រាល់ពេលមានការ Insert ថ្មីចូល Database
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = "PENDING"; // កំណត់ឱ្យទៅជា PENDING ជាមុន
        }
        this.orderDate = LocalDateTime.now(); // ចាប់យកម៉ោងបច្ចុប្បន្ន (ដោះស្រាយបញ្ហា 1970)
    }

//    private LocalDateTime order_date = LocalDateTime.now();

    // ២. សំខាន់បំផុត៖ បន្ថែម Setter ឱ្យបានត្រឹមត្រូវ
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("order")
    private List<OrderItem> items;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
