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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Rafael
 */
@ManagedBean
@SessionScoped
public class PetController {

    private Pet petCadastro;
    private Pet selection;
    private String modalType;
    private String codigoUnico;
    private String codigoUnicoPetCompartilhado;
    private int petBuscaCodigo;
    private Pet petPesquisado;

    private List<Pet> petsEncontrados;
    private String nomePetParaBuscar;

    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;

    @PostConstruct
    public void init() {
        this.petCadastro = new Pet();
        this.modalType = "create";
    }

    public String inserir() {

        Tutor tutorLogado = loginController.getLogado();

        CompartilhamentoPet compartilhamentopet = new CompartilhamentoPet();

        if (tutorLogado != null) {

            compartilhamentopet.setTutor(tutorLogado);
            compartilhamentopet.setPet(this.petCadastro);

            ManagerDao.getCurrentInstance().insert(this.petCadastro);
            ManagerDao.getCurrentInstance().insert(compartilhamentopet);

            this.petCadastro = new Pet();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pet cadastrado com sucesso!"));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar pet", "Tutor logado não disponível."));
        }

        return "";
    }

    public List<Pet> readPets() {
        Tutor tutorLogado = loginController.getLogado();
        List<Pet> pets = null;

        String jpql = "select cp.pet from CompartilhamentoPet cp where cp.tutor = :tutor";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tutor", tutorLogado);

        pets = ManagerDao.getCurrentInstance().reads(jpql, Pet.class, parameters);
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

    public String compartilharPet(String codigoUnico) {

        Pet pet = (Pet) ManagerDao.getCurrentInstance().read("select p from Pet p where p.codigoUnico = '" + codigoUnico + "'", Pet.class).get(0);
        CompartilhamentoPet compartilhamentoPet;
        Tutor tutor;

        if (pet != null && !codigoUnico.isEmpty()) {
            compartilhamentoPet = new CompartilhamentoPet();
            tutor = loginController.getLogado();

            compartilhamentoPet.setPet(pet);
            compartilhamentoPet.setTutor(tutor);

            ManagerDao.getCurrentInstance().insert(compartilhamentoPet);

            return "menututor.xhtml";
        }

        return null;
    }

    public List<CompartilhamentoPet> tutoresDoSelection() {
        return ManagerDao.getCurrentInstance().read("select cp from CompartilhamentoPet cp where cp.pet.codigo = " + this.selection.getCodigo(), Pet.class);

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

    public void upload(FileUploadEvent e) throws IOException {
        byte[] blob = new byte[(int) e.getFile().getSize()];
        e.getFile().getInputstream().read(blob);

        this.petCadastro.setImagem(blob);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Imagem carregada com sucesso."));
    }

    public void uploadChange(FileUploadEvent e) throws IOException {
        byte[] blob = new byte[(int) e.getFile().getSize()];
        e.getFile().getInputstream().read(blob);

        this.selection.setImagem(blob);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Imagem carregada com sucesso."));
    }

    public String getImagemPet() {
        if (this.selection != null) {
            byte[] blob = this.selection.getImagem();
            return blob != null ? Base64.getEncoder().encodeToString(blob) : "";
        } else {
            return "";
        }
    }

    public String formatImagemIndex(byte[] blob) {
        return blob != null ? Base64.getEncoder().encodeToString(blob) : "";
    }

    public String getGraphicImage() {
        byte[] blob = this.petCadastro.getImagem();
        return blob != null ? Base64.getEncoder().encodeToString(blob) : "";
    }

    private Tutor tutorLogadoSession() {
        return ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true))
                .getAttribute("loginController")).getTutorLogado();
    }

    public String imagemPetPesquisa(Pet pet) {
        byte[] blob = pet.getImagem();
        return blob != null ? Base64.getEncoder().encodeToString(blob) : "";
    }

    public String imagemPetPesquisado() {
        byte[] blob = buscarPesquisaPet().getImagem();
        return blob != null ? Base64.getEncoder().encodeToString(blob) : "";

    }

    public List<CompartilhamentoPet> tutorPetPesquisa(Pet pet) {
        return ManagerDao.getCurrentInstance().read("select cp from CompartilhamentoPet cp where cp.pet.codigo =" + buscarPesquisaPet().getCodigo(), Pet.class);
    }

    public List<CompartilhamentoPet> tutorPetPesquisado(Pet pet) {
        return ManagerDao.getCurrentInstance().read("select cp from CompartilhamentoPet cp where cp.pet.codigo =" + pet.getCodigo(), Pet.class);
    }

    public int getPetBuscaCodigo() {
        return petBuscaCodigo;
    }

    public void setPetBuscaCodigo(int petBuscaCodigo) {
        this.petBuscaCodigo = petBuscaCodigo;
    }

    public Pet getPetPesquisado() {
        return petPesquisado;
    }

    public void setPetPesquisado(Pet petPesquisado) {
        this.petPesquisado = petPesquisado;
    }

    public List<Pet> pesquisarPet(String petNome) {

        List<Pet> petsEncontrados = ManagerDao.getCurrentInstance().read("select p from Pet p where p.nome = '" + petNome + "'", Pet.class);

        return petsEncontrados;

    }

    public Pet buscarPesquisaPet() {
        return (Pet) ManagerDao.getCurrentInstance().read("select p from Pet p where p.codigo = " + this.petBuscaCodigo, Pet.class).get(0);
    }

    public void seguir() {
        this.petPesquisado = buscarPesquisaPet();

        if (petPesquisado != null) {
            this.selection.getSeguindo().add(this.petPesquisado);
            this.petPesquisado.getSeguidores().add(this.selection);
            ManagerDao.getCurrentInstance().update(this.selection);
            ManagerDao.getCurrentInstance().update(this.petPesquisado);

        }
    }

    public void deixarDeSeguir() {
        this.petPesquisado = buscarPesquisaPet();

        if (petPesquisado != null) {
            this.selection.getSeguindo().remove(this.petPesquisado);
            this.petPesquisado.getSeguidores().remove(this.selection);
            ManagerDao.getCurrentInstance().update(this.selection);
            ManagerDao.getCurrentInstance().update(this.petPesquisado);

        }
    }

    public boolean jaESeguidor() {
        this.petPesquisado = buscarPesquisaPet();

        for (Pet pet : this.selection.getSeguindo()) {
            if (pet.getCodigo() == this.petPesquisado.getCodigo()) {
                return true;
            }
        }

        return false;
    }

}
