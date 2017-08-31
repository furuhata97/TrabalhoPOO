/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.util.logging.Logger;
import view.SelecionaPerfilView;

/**
 *
 * @author gusta
 */
public class Principal {
    
    static final Logger log = Logger.getLogger(Principal.class.getName());
    
        public static void main(String[] args) {
        iniciaSistema();
    }

     //Abre a primeira interface para seleção de usuário e redireciona para a view correspondente.
    public static void iniciaSistema() {
        SelecionaPerfilController controller = new SelecionaPerfilController(new SelecionaPerfilView());
        controller.controla();
    }

    //construtor padrão
    public Principal() {
    }
}
