/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import util.enumeration.EmployeeRole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**1
 *
 * @author ryanl
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    @NotNull
    private String name;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    private String username;
    @Column(nullable = false, length = 64)
    @NotNull
    private String password;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;
    
    @ManyToOne
    private Outlet outlet; //Each employee has only 1 outlet, 1 outlet has many employees
    
    @OneToMany(mappedBy = "employeeAssigned", fetch = FetchType.EAGER)
    private List<TransitDriverDispatchRecord> dispatches;
    

    public Employee() {
        this.dispatches = new ArrayList<>();
    }
    // removed Outlet from constructor. Since it is entity relationship and not attribute
    public Employee(String name, String username, String password, EmployeeRole employeeRole) {
        this();
        this.name = name;
        this.username = username;
        this.password = password;
        this.employeeRole = employeeRole;
        //this.outlet = outlet;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeRole getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(EmployeeRole employeeRole) {
        this.employeeRole = employeeRole;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + this.getId() + "]" + " : " + this.getName();
    }

    /**
     * @return the dispatches
     */
    public List<TransitDriverDispatchRecord> getDispatches() {
        return dispatches;
    }

    /**
     * @param dispatches the dispatches to set
     */
    public void setDispatches(List<TransitDriverDispatchRecord> dispatches) {
        this.dispatches = dispatches;
    }
    
}
