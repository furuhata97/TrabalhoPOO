/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import model.Medico;
import model.Paciente;
import view.CadastraAlteraProntuarioView;

/**
 *
 * @author gusta
 */
public class CadastraAlteraProntuarioController {
    
    private CadastraAlteraProntuarioView view;
    private Paciente paciente;
    private Medico medico;
    
    //Construtor padrão
    public CadastraAlteraProntuarioController(){
        
    }
    
    //Construtor parâmetrizado
    public CadastraAlteraProntuarioController(CadastraAlteraProntuarioView view, Medico medico, Paciente paciente){
        this.view = view;
        this.medico = medico;
        this.paciente = paciente;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Com a view aberta, o usuário escolhe as informações que deseja e clica em OK
        
        //Quando o botão OK é clicado, as informações selecionadas na view são setadas no paciente. Por fim, é feita a chamada do método
        // para inclusão/alteração de prontuário no banco de dados, e a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            this.paciente.setSintomas(view.getSintomas().getText());
            this.paciente.setDiagnostico(view.getDiagnostico().getText());
            this.paciente.setTratamento(view.getTratamento().getText());
            this.medico.adicionaProntuarioPaciente(paciente);
            view.dispose();
        });
        this.view.setVisible(true);
    }
}
