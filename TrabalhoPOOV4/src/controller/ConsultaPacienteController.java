package controller;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Consulta;
import model.Paciente;
import model.Secretaria;
import view.AtualizaConsultaView;
import view.BuscaConsultaView;
import view.CancelaConsultaView;
import view.ConsultaPacienteView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class ConsultaPacienteController {
    private Secretaria secretaria;
    private ConsultaPacienteView view;
    private Paciente paciente;
    private int tipo;
    
    /* Contrutor padrão */
    public ConsultaPacienteController() {
    }
    
    /*Contrutor parametrizado*/
    public ConsultaPacienteController(ConsultaPacienteView view, Secretaria secretaria, int tipo) {
        this.secretaria = secretaria;
        this.view = view;
        this.tipo = tipo;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Quando as opções de Atualização, Cancelamento e Busca de consultas são acionadas, é necessário passar um paciente para que as consultas do paciente sejam encontradas
        //A view para digitação do CPF do paciente é aberta
        
        //Quando o botão OK é clicado, é feita uma verificação do CPF. Caso nenhum CPF tenha sido digitado, é exibida uma mensagem ao usuário. Caso o CPF seja digitado no
        //formato correto, é feita uma busca pelo paciente. Caso não haja pacientes com o CPF digitado, uma mensagem é exibida ao usuário.
        //Caso o paciente seja encontrado, é chamado o método que cria uma lista com todas as consultas que possuem o paciente com o mesmo CPF digitado anteriormente
        //Essa lista de consultas será passada para a atualização, cancelamento ou busca de consultas. Para diferenciar qual tipo de ação será realizada, é passado através
        //do construtor dessa classe, um inteiro que representa o tipo de ação. Caso se deseje atualizar consultas, é passado para o construtor dessa classe o inteiro 1
        //Assim, a classe sabe que deve chamar atualização de consultas caso a variável tipo seja = 1. Caso seja passado 2, será chamado busca de consultas. Caso seja
        //passado 3, será chamado cancelamento de consultas
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            if (!view.getCpfPaciente().getText().equals("")){
                paciente = secretaria.getPacienteCPF(Long.parseLong(view.getCpfPaciente().getText()));
                if(paciente == null){
                    JOptionPane.showMessageDialog(null, "Não há pacientes cadastrados com o CPF digitado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                else{
                    List<Consulta> consultasCPF = consultasPorCpf(secretaria.listaConsultas(), paciente);
                    view.dispose();
                    if(tipo == 1){
                        new AtualizaConsultaController(new AtualizaConsultaView(), secretaria, consultasCPF).controla();
                    }
                    if(tipo == 2){
                        new BuscaConsultaController(new BuscaConsultaView(), secretaria, consultasCPF).controla();
                    }
                    if(tipo == 3){
                        new CancelaConsultaController(new CancelaConsultaView(), secretaria, consultasCPF).controla();
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(null, "Nenhum CPF digitado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
            
        });
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view da Secretária é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        this.view.setVisible(true);
    }
    
    
    public List<Consulta> consultasPorCpf(List<Consulta> consultas, Paciente p){
        //Retorna uma lista de consultas de acordo com o CPF do paciente
        List<Consulta> consultasCPF = new LinkedList<Consulta>();
        for(Consulta c: consultas){
            if(c.getPaciente().getCpf() == p.getCpf()){
                consultasCPF.add(c);
            }
        }
        return consultasCPF;
    }
}
