/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Poh San Wang
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<MakeModel> listOfMakeModelsIncluded;

    @OneToMany(mappedBy = "carCategory", fetch = FetchType.EAGER)
    private List<RentalRate> rentalRates;

    public CarCategory() {
        this.listOfMakeModelsIncluded = new ArrayList<>();
        this.rentalRates = new ArrayList<>();
    }

    public CarCategory(String name) {
        this.name = name;
        this.listOfMakeModelsIncluded = new ArrayList<>();
        this.rentalRates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MakeModel> getListOfMakeModelsIncluded() {
        return listOfMakeModelsIncluded;
    }

    public void setListOfMakeModelsIncluded(List<MakeModel> listOfMakeModelsIncluded) {
        this.listOfMakeModelsIncluded = listOfMakeModelsIncluded;
    }

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
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + id + " ]";
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public List<RentalRate> getRentalRates(boolean fetchEager) {
        rentalRates.size();
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

}
