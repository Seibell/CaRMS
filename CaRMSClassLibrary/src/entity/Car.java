/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import util.enumeration.CarStatus;

/**
 *
 * @author Poh San Wang
 */
@Entity
public class Car implements Serializable, Comparable<Car> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    private String licensePlate;
    
    @Column(nullable = false)
    private boolean disabled = false;
    @Column(nullable = false)
    private boolean used = false;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private MakeModel makeModel;
    
    @Column(nullable = false, length = 32)
    @NotNull
    private String colour; // we may want to make ENUM for this>
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CarStatus carStatus;
    
    @ManyToOne
    private Outlet locationOutlet;
    
    @ManyToOne // a customer can rent many cars?
    private Customer locationCustomer;

    public Car() {
    }

    public Car(String licensePlate, String colour) {
        this.licensePlate = licensePlate;
        this.colour = colour;
        this.carStatus = CarStatus.IN_OUTLET;
    }
    
    @Override
    public int compareTo(Car other) {
        if (this.getMakeModel().getCategory().equals(other.getMakeModel().getCategory())) {
            return licensePlate.compareTo(other.getLicensePlate());
        } else {
            return this.getMakeModel().getCategory().toString().compareTo(other.getMakeModel().getCategory().toString());
        }
    }
    
    
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public MakeModel getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(MakeModel makeModel) {
        this.makeModel = makeModel;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    
    public Outlet getLocationOutlet() {
        return locationOutlet;
    }

    public void setLocationOutlet(Outlet locationOutlet) {
        this.locationOutlet = locationOutlet;
    }

    public Customer getLocationCustomer() {
        return locationCustomer;
    }

    public void setLocationCustomer(Customer locationCustomer) {
        this.locationCustomer = locationCustomer;
    }
    
    
    
    // private Pair<Outlet, Customer> location;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%10s%40s%40s%40s%20s%20s%40s%40s\n", this.id, this.licensePlate, this.makeModel.getName().toString(), this.makeModel.getCategory().getName().toString(), this.colour.toString(), this.carStatus.toString(), this.locationOutlet, this.locationCustomer);
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * @return the carStatus
     */
    public CarStatus getCarStatus() {
        return carStatus;
    }

    /**
     * @param carStatus the carStatus to set
     */
    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }
    
}
