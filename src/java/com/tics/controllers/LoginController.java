/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tics.controllers;

import com.tics.model.dao.ManagerDao;
import com.tics.model.negocio.Tutor;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;

/**
 *
 * @author Rafael
 */

@ManagedBean
@SessionScoped
public class LoginController {
    
    private Tutor logado;
    
    public String logar(String usuario, String senha){
        
    try{
        Tutor aux = (Tutor)ManagerDao.getCurrentInstance()
                .read("select t from Tutor t where t.usuario=\""+usuario+"\"and t.senha=\""+ senha+"\"", Tutor.class).get(0);
    
        this.logado = aux;
        
        return "menututor.xhtml";
        
    }catch(Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao logar", "Usuário e/ou senha estão incorretos"));
            
            return null;   
    }
    
}
    
    public String logout(){
        this.logado = null;
        return "login";
    }

    public Tutor getLogado() {
        return logado;
    }

    public void setLogado(Tutor logado) {
        this.logado = logado;
    }

}
