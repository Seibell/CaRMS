/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Poh San Wang
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Employee employeeAssigned;
    
    @ManyToOne(optional = true) // do i have to specify for unidirectional???
    @JoinColumn(nullable = true)
    private Car carMoved;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet fromOutlet;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet toOutlet;
    
    @Column(nullable = false)
    private boolean complete;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Reservation reservation;
    
    public TransitDriverDispatchRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployeeAssigned() {
        return employeeAssigned;
    }

    public void setEmployeeAssigned(Employee employeeAssigned) {
        this.employeeAssigned = employeeAssigned;
    }

    public Car getCarMoved() {
        return carMoved;
    }

    public void setCarMoved(Car carMoved) {
        this.carMoved = carMoved;
    }

    public Outlet getFromOutlet() {
        return fromOutlet;
    }

    public void setFromOutlet(Outlet fromOutlet) {
        this.fromOutlet = fromOutlet;
    }

    public Outlet getToOutlet() {
        return toOutlet;
    }

    public void setToOutlet(Outlet toOutlet) {
        this.toOutlet = toOutlet;
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
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%10s%40s%40s%40s%40s%10s%40s",
                getId(), getFromOutlet(), getToOutlet(), getCarMoved().getLicensePlate(),
                this.getEmployeeAssigned() == null ? "null" : this.getEmployeeAssigned().toString(),
                isComplete(), getReservation().getId());
    }

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}
