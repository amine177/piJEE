/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.overpowered.byusforus.entities.users;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author EliteBook
 */
@Entity
@DiscriminatorValue(value = "CANDIDATE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Candidate extends Professional implements Serializable {

    private static final long serialVersionUID = 28L;



    




}
