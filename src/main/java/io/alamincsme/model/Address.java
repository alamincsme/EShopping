package io.alamincsme.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Building name must  contain at least 5 characters")
    private String buildingName;


    @NotBlank
    @Size(min = 5 , message = "Street name must contain at lest 5 character")
    private String street;

    @NotBlank
    @Size(min = 6, message = "Post code must contain at least 6 characters")
    private String postCode;

    @NotBlank
    @Size(min = 2, message = "State must contain at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 4, message = "City name must contain at least 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2 , message = "Country name must contain at least 2 characters")
    private String country;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String buildingName, String street, String postCode, String state, String city, String country, List<User> users) {
        this.buildingName = buildingName;
        this.street = street;
        this.postCode = postCode;
        this.state = state;
        this.city = city;
        this.country = country;
        this.users = users;
    }


}
