/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import model.Medico;
import model.Paciente;
import view.CadastraAlteraDadosAdicionaisView;

/**
 *
 * @author gusta
 */
public class CadastraAlteraDadosAdicionaisController {
    
    private CadastraAlteraDadosAdicionaisView view;
    private Paciente paciente;
    private Medico medico;
    
    //Construtor padrão
    public CadastraAlteraDadosAdicionaisController(){
        
    }
    
    //Construtor parâmetrizado
    public CadastraAlteraDadosAdicionaisController(CadastraAlteraDadosAdicionaisView view, Medico medico, Paciente paciente){
        this.view = view;
        this.medico = medico;
        this.paciente = paciente;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Com a view aberta, o usuário escolhe as informações que deseja e clica em OK
        
        //Quando o botão OK é clicado, as informações selecionadas na view são setadas no paciente. Por fim, é feita a chamada do método
        // para inclusão/alteração de dados adicionais no banco de dados, e a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            this.paciente.setFuma(view.getFuma().getSelectedItem().toString());
            this.paciente.setBebe(view.getBebe().getSelectedItem().toString());
            this.paciente.setColesterol(view.getColesterol().getSelectedItem().toString());
            this.paciente.setDiabete(view.getDiabetes().getSelectedItem().toString());
            this.paciente.setdCardiaca(view.getdCardiaca().getSelectedItem().toString());
            this.paciente.setAlergias(view.getAlergias().getText());
            this.paciente.setCirurgias(view.getCirurgias().getText());
            this.medico.adicionaDadosPaciente(paciente);
            view.dispose();
        });
        this.view.setVisible(true);
    }
}
