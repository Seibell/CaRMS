/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Poh San Wang
 */
@Entity
public class MakeModel implements Serializable, Comparable<MakeModel> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
    
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    private String name;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private CarCategory category;
    
    @Column(nullable = false)
    private boolean used = false;
    @Column(nullable = false)
    private boolean disabled = false;

    public MakeModel() {
    }

    public MakeModel(String name) {
        this.name = name;
    }
    
    @Override
    public int compareTo(MakeModel other) {
        if (this.category.toString().equals(other.category.toString())) {
            return this.name.compareTo(other.name);
        } 
        return this.category.toString().compareTo(other.category.toString());
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
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
        if (!(object instanceof MakeModel)) {
            return false;
        }
        MakeModel other = (MakeModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%10s%40s%40s%20s", getId(), getName(), getCategory().getName(), isDisabled());
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
    
}
