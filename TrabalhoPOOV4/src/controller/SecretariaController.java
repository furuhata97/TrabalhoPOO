/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.awt.event.ActionEvent;
import model.Secretaria;
import view.BuscaPacienteView;
import view.CadastraPacienteView;
import view.SecretariaView;
import view.SelecionaPerfilView;
import view.AtualizaPacienteView;
import view.ConsultaPacienteView;
import view.ConsultaPorDataView;
import view.RemovePacienteView;
import view.NovaConsultaView;

/**
 *
 * @author gusta
 */
public class SecretariaController {
    
    private Secretaria secretaria;
    private SecretariaView view;

    /* Contrutor padrão */
    public SecretariaController() {
    }
    
    /*Contrutor parametrizado*/
    public SecretariaController(Secretaria secretaria, SecretariaView view) {
        this.secretaria = secretaria;
        this.view = view;
        this.view.setVisible(true);
    }
    
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view de Seleção de perfil é aberta novamente
        view.getBtnVoltarSelecaoPerfil().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SelecionaPerfilController(new SelecionaPerfilView()).controla();
        });
        
        
       //Caso o botão Cadastro de paciente seja clicado, a view de cadastro de paciente é aberta
        view.getBtnCadastraPaciente().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new CadastraPacienteController(new CadastraPacienteView(), secretaria).controla();
        });
        
        //Caso o botão Busca de paciente seja clicado, a view de busca de paciente é aberta
        view.getBtnBuscaPaciente().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new BuscaPacienteController(new BuscaPacienteView(), secretaria).controla();
        });
        
        //Caso o botão Atualização de paciente seja clicado, a view de atualização de paciente é aberta
        view.getBtnAtualizaPaciente().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new AtualizaPacienteController(new AtualizaPacienteView(), secretaria).controla();
        });
        
        //Caso o botão Remover de paciente seja clicado, a view de remover de paciente é aberta
        view.getBtnRemovePaciente().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new RemovePacienteController(new RemovePacienteView(), secretaria).controla();
        });
        
        //Caso o botão Nova Consulta seja clicado, a view de cadastro de consulta é aberta
        view.getBtnNovaConsulta().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new NovaConsultaController(new NovaConsultaView(), secretaria).controla();
        });
        
        //Caso o botão Alterar Consulta seja clicado, a view de atualização de consulta é aberta
        view.getBtnAlteraConsulta().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new ConsultaPacienteController(new ConsultaPacienteView(), secretaria, 1).controla();
        });
        
        //Caso o botão Buscar Consulta seja clicado, a view de busca de consulta é aberta
        view.getBtnBuscaConsulta().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new ConsultaPacienteController(new ConsultaPacienteView(), secretaria, 2).controla();
        });
        
        //Caso o botão Cancelar Consulta seja clicado, a view de cancelar consulta é aberta
        view.getBtnCancelaConsulta().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new ConsultaPacienteController(new ConsultaPacienteView(), secretaria, 3).controla();
        });
        
        //Caso o botão Email seja clicado, a view de envio de notificações via email é aberta
        view.getBtnEmail().addActionListener((ActionEvent actionEvent) -> {
            new ConsultaPorDataController(new ConsultaPorDataView(view), secretaria, false).controla();
        });
        
        //Caso o botão telefone seja clicado, a view de envio de notificações via sms é aberta
        view.getBtnTelefone().addActionListener((ActionEvent actionEvent) -> {
            new ConsultaPorDataController(new ConsultaPorDataView(view), secretaria, true).controla();;
        });
    }
    
}
