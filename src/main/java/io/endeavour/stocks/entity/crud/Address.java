package io.endeavour.stocks.entity.crud;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity()
@Table(name = "address", schema = "endeavour_test_area")
public class Address {

    @Column(name = "address_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressID;
    @Column(name = "line1")
    private String line1;
    @Column(name = "line2")
    private String line2;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip_code")
    private Integer zipCode;

    public int getAddressID() {
        return addressID;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private Person person;

    //Custom get method to get person name field
    public String getPersonName(){
        return person.getFirstName()+" "+person.getLastName();
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressID=" + addressID +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return addressID == address.addressID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressID);
    }
}
