/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tics.model.negocio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Rafael
 */
@Entity
@Table(name = "compartilhamento_pet")
public class CompartilhamentoPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

      
    @ManyToOne
    @JoinColumn(name = "tutor_recebedor_codigo")
    private Tutor tutorRecebedor;

    @Column(name = "codigo_unico")
    private String codigoUnico;

    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

  
    public Tutor getTutorRecebedor() {
        return tutorRecebedor;
    }

    public void setTutorRecebedor(Tutor tutorRecebedor) {
        this.tutorRecebedor = tutorRecebedor;
    }

}
