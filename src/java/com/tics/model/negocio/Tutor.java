/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tics.model.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Rafael
 */
@Entity
@Table(name = "tutor")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    private String usuario;
    private String senha;
    private String email;
    private boolean mamae;
    
    @Lob
    private byte[] imagem;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "tutorRecebedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompartilhamentoPet> compartilhamentosRecebidos = new ArrayList<>();

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }


    public List<CompartilhamentoPet> getCompartilhamentosRecebidos() {
        return compartilhamentosRecebidos;
    }

    public void setCompartilhamentosRecebidos(List<CompartilhamentoPet> compartilhamentosRecebidos) {
        this.compartilhamentosRecebidos = compartilhamentosRecebidos;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMamae() {
        return mamae;
    }

    public void setMamae(boolean mamae) {
        this.mamae = mamae;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    
}
