/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tics.controllers;

import com.tics.model.dao.ManagerDao;
import com.tics.model.negocio.CompartilhamentoPet;
import com.tics.model.negocio.Tutor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Rafael
 */
@ManagedBean
@SessionScoped
public class TutorController {

    private Tutor tutorCadastro;
    private Tutor selecion;
    private String modalType;

    @PostConstruct
    public void init() {
        this.tutorCadastro = new Tutor();
        this.modalType = "create";

    }

    public void inserir(String confirma) {

        if (!this.tutorCadastro.getSenha().equals(confirma)) {

            FacesContext.getCurrentInstance().addMessage("formCadTutor:txtSenha",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de validação", "A senha não confirma"));

            return;
        }
        ManagerDao.getCurrentInstance().insert(this.tutorCadastro);

        this.tutorCadastro = new Tutor();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Tutor cadastrado com sucesso!"));

    }

    public List<Tutor> readTutors() {

        List<Tutor> tutors = null;

        tutors = ManagerDao.getCurrentInstance()
                .read("select t from Tutor t", Tutor.class);

        return tutors;

    }

    public Tutor getTutorCadastro() {
        return tutorCadastro;
    }

    public void setTutorCadastro(Tutor tutorCadastro) {
        this.tutorCadastro = tutorCadastro;
    }

    public Tutor getSelecion() {
        return selecion;
    }

    public void setSelecion(Tutor selecion) {
        this.selecion = selecion;
    }

    public String alterar() {

        ManagerDao.getCurrentInstance().update(this.selecion);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "Perfil alterado com sucesso!"));

        return "tutors";

    }

    public void editarPerfil() {

        ManagerDao.getCurrentInstance().update(this.selecion);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "Tutor alterado com Sucesso"));

    }

    public void deletar() {

        ManagerDao.getCurrentInstance().delete(this.selecion);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso!", "Infelizmente você deletou seu tutor"));

    }

    public void alterarSenha(String senha, String novaSenha, String confirma) {

        //código para recuperar qualquer atributo na sessão
        Tutor tLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true))
                .getAttribute("loginController")).getLogado();

        if (!tLogado.getSenha().equals(senha)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A senha digitada está incorreta. "));
            return;
        }

        if (!novaSenha.equals(confirma)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A nova senha não bate com a confirmação. "
                            + "Por favor, digite novamente de forma correta"));
            return;
        }

        tLogado.setSenha(novaSenha);

        ManagerDao.getCurrentInstance().update(tLogado);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Senha alterada com sucesso!"));
    }

    public List<String> imagensDosTutores(List<CompartilhamentoPet> tutores) {
        List<String> imagens = new ArrayList<>();
        for (CompartilhamentoPet cp : tutores) {
            byte[] blob = cp.getTutor().getImagem();
            imagens.add(blob != null ? Base64.getEncoder().encodeToString(cp.getTutor().getImagem()) : "");
        }

        return imagens;
    }

    public void modalType(String type) {
        this.modalType = type;
    }

    public String getModalType() {
        return modalType;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        byte[] im = new byte[(int) event.getFile().getSize()];
        event.getFile().getInputstream().read(im);
        Tutor tutorLogado = tutorLogadoSession();
        tutorLogado.setImagem(im);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage("Imagem Salva com sucesso!"));

    }

    public String getImagemTutor() {
        Tutor tutorLogado = tutorLogadoSession();
        if (tutorLogado != null) {
            byte[] im = tutorLogado.getImagem();
            return im != null ? Base64.getEncoder().encodeToString(im) : "";
        } else {
            return "";
        }
    }

    public String getGraphicImage() {
        Tutor tutorLogado = tutorLogadoSession();
        if (tutorLogado != null) {
            byte[] im = tutorLogado.getImagem();
            return im != null ? Base64.getEncoder().encodeToString(im) : "";
        } else {
            return ""; // ou alguma string padrão se tutorLogado for nulo
        }
    }

    private Tutor tutorLogadoSession() {
        return ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true))
                .getAttribute("loginController")).getLogado();
    }

}
