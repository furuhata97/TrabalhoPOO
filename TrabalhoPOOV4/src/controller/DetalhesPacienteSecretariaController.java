/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import javax.swing.JOptionPane;
import model.Consulta;
import model.Paciente;
import model.Secretaria;
import view.DetalhesPacienteSecretariaView;

/**
 *
 * @author gusta
 */
public class DetalhesPacienteSecretariaController {
    
    private Paciente paciente;
    private DetalhesPacienteSecretariaView view;
    private boolean remover = false;
    private Secretaria secretaria;
    
    /* Contrutor padrão */
    public DetalhesPacienteSecretariaController() {
    }

    /*Contrutor parametrizado.*/
    public DetalhesPacienteSecretariaController(DetalhesPacienteSecretariaView view, Paciente paciente) {
        this.paciente = paciente;
        this.view = view;
    }
    
    /*Construtor parametrizado. Utilizado na busca e exclusão de paciente*/
    public DetalhesPacienteSecretariaController(DetalhesPacienteSecretariaView view, Paciente paciente, boolean remover, Secretaria secretaria) {
        this.paciente = paciente;
        this.view = view;
        this.remover = remover;
        this.secretaria = secretaria;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla() {
        
        //Seta os campos da view com as informações do paciente
        view.getNomePaciente().setText(paciente.getNome());
        view.getCpfPaciente().setText(paciente.getCpf().toString());
        view.getCidadePaciente().setText(paciente.getCidade());
        view.getEnderecoPaciente().setText(paciente.getEndereco());
        view.getNumCasaPaciente().setText(String.valueOf(paciente.getNumeroCasa()));
        view.getComplementoPaciente().setText(paciente.getComplemento());
        view.getEstadoPaciente().setText(paciente.getEstado());
        view.getNascimentoPaciente().setText(paciente.getDataNascimento().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getConvenioPaciente().setText(paciente.getConvenio());
        view.getTelefonePaciente().setText(paciente.getTelefone());
        view.getEmailPaciente().setText(paciente.getEmail());
        
        //Caso a ação seja a de visualizar detalhes do paciente, o botão Cancelar é ocultado. Caso seja ação de Exclusão de paciente, o botão fica visível
        if(!remover){
            view.getCancelar().setVisible(false);
        }
        
        
        //As ações do botão Ok dependem do valor de "remover", se é true ou false
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            
            //Caso "remover" seja false, significa que está ocorrendo apenas uma busca por paciente, logo ao ser apertado o botão Ok, a view é fechada 
            if(!remover)
                view.dispose();
            
            //Caso "remover" seja true, significa que a view tem capacidade de remover o paciente ao ser pressionado o botão Ok
            else{
                
                //Para verificar se o paciente pode ser removido, é obtida uma lista com todas as consultas cadastradas. Essa lista de consultas é percorrida
                //verificando se alguma consulta está associdada ao paciente que se deseja remover. Caso esteja, o paciente não será removido e uma mensagem será
                //exibida ao usuário. Caso nenhuma consulta esteja associada ao paciente, este será excluído através da chamada do método removePaciente e uma mensagem
                //de sucesso será exibida ao usuário, fechando a view logo em seguida
                List<Consulta> consultas = secretaria.listaConsultas();
                boolean verificaPacienteTemConsulta = false;
                for(int i = 0; i<consultas.size(); i++){
                    if(consultas.get(i).getPaciente().getCpf() == paciente.getCpf()){
                        JOptionPane.showMessageDialog(view, "Paciente com consulta não pode ser removido.", "Erro", JOptionPane.WARNING_MESSAGE);
                        verificaPacienteTemConsulta = true;
                        break;
                    }
                }
                if (!verificaPacienteTemConsulta){
                    secretaria.removePaciente(paciente);
                    JOptionPane.showMessageDialog(view, "Paciente removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    view.dispose();
                }
            }
        });
        
        //Caso o botão cancelar seja apertado, a view é fechada. O botão só estará visível caso a variável "remover" seja true
        view.getCancelar().addActionListener((ActionEvent actionEvent) -> {
                view.dispose();
        });
        this.view.setVisible(true);
    }
}
