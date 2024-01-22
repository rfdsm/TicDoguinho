/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tics.controllers;

import com.tics.model.dao.ManagerDao;
import com.tics.model.negocio.CompartilhamentoPet;
import com.tics.model.negocio.Pet;
import com.tics.model.negocio.Tutor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rafael
 */
@ManagedBean
@SessionScoped
@ViewScoped
public class PetController {

    private Pet petCadastro;
    private Pet selection;
    private String modalType;
    private String codigoUnico;
    private String codigoUnicoPetCompartilhado;

    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;

    @PostConstruct
    public void init() {
        this.petCadastro = new Pet();
        this.modalType = "create";
    }

    public String inserir() {

        Tutor tutorLogado = loginController.getLogado();

        if (tutorLogado != null) {

            petCadastro.setTutor(tutorLogado);
            petCadastro.setCodigoUnico(gerarCodigoUnico());
            tutorLogado.getPets().add(petCadastro);
            ManagerDao.getCurrentInstance().insert(petCadastro);
            this.petCadastro = new Pet();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pet cadastrado com sucesso!"));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar pet", "Tutor logado não disponível."));
        }

        return "";
    }

    private String gerarCodigoUnico() {

        return UUID.randomUUID().toString();
    }

    public List<Pet> readPets() {

        List<Pet> pets = null;
        pets = ManagerDao.getCurrentInstance().read("select p from Pet p", Pet.class);
        return pets;

    }

    public Pet getPetCadastro() {
        return petCadastro;
    }

    public void setPetCadastro(Pet petCadastro) {
        this.petCadastro = petCadastro;
    }

    public Pet getSelection() {
        return selection;
    }

    public void setSelection(Pet selection) {
        this.selection = selection;
    }

    public String getModalType() {
        return modalType;
    }

    public void setModalType(String modalType) {
        this.modalType = modalType;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void alterar() {

        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso", "Pet alterado com sucesso"));
    }

    public void deletar() {
        ManagerDao.getCurrentInstance().delete(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage("Infelizmente você deletou o seu Pet!"));
    }

    public String compartilharPet() {

        Tutor tutorLogado = loginController.getLogado();
        Pet petSelecionado = selection;

        if (codigoUnicoPetCompartilhado != null && !codigoUnicoPetCompartilhado.isEmpty()) {

            List<CompartilhamentoPet> compPets = ManagerDao.getCurrentInstance().read("select cp from CompartilhamentoPet cp where cp.codigoUnico ='" + codigoUnicoPetCompartilhado +"'",CompartilhamentoPet.class);
            if(compPets.size() == 2){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pet atingiu o limite de tutores.", ""));
                
                return null;
            }
            CompartilhamentoPet compartilhamentoPet = new CompartilhamentoPet();
            compartilhamentoPet.setCodigoUnico(codigoUnicoPetCompartilhado);
            compartilhamentoPet.setTutorRecebedor(tutorLogado); 

            ManagerDao.getCurrentInstance().insert(compartilhamentoPet);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pet Compartilhado com sucesso", "Código único fornecido."));

            codigoUnicoPetCompartilhado = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro Código único não fornecido.", "Código único não fornecido."));
        }

        return null;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public String getCodigoUnicoPetCompartilhado() {
        return codigoUnicoPetCompartilhado;
    }

    public void setCodigoUnicoPetCompartilhado(String codigoUnicoPetCompartilhado) {
        this.codigoUnicoPetCompartilhado = codigoUnicoPetCompartilhado;
    }

}
