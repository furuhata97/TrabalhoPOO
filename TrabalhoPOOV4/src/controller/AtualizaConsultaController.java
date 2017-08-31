package controller;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Paciente;
import model.Secretaria;
import org.apache.commons.beanutils.PropertyUtils;
import view.AtualizaConsultaView;
import view.DetalhesConsultaView;
import view.SecretariaView;


public class AtualizaConsultaController {
    private Secretaria secretaria;
    private AtualizaConsultaView view;
    private List<Consulta> consultas;
    
    /* Contrutor padrão */
    public AtualizaConsultaController() {
    }

    /*Contrutor parametrizado.*/
    public AtualizaConsultaController(AtualizaConsultaView view, Secretaria secretaria, List<Consulta> consultas) {
        this.secretaria = secretaria;
        this.view = view;
        this.consultas = consultas;
        view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Método que inclui as consultas em uma tabela na view
        atualizaTabelaConsultas();
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view da Secretária é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        
        //Caso o botão alterar seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão alterar foi clicado sem que nenhuma linha da
        //tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhuma consulta foi selecionado na tabela. Caso não haja consultas disponíveis 
        //para atualização(tabela vazia) e o botão alterar seja clicado, é exibida uma mensagem dizendo que não há consultas disponíveis para atualização.
        //Caso uma linha da tabela seja selecionada e o botão alterar seja clicado, é aberta a view para atualização de consultas.
        view.getBtnAlterar().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabelaConsulta().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabelaConsulta().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Consulta co = secretaria.getConsultaCodigo((Long) view.getTabelaConsulta().getModel().getValueAt(linhaSelecionada, 0));
                    new DetalhesConsultaController(new DetalhesConsultaView(view), co, 1, secretaria).controla();
                    atualizaTabelaConsultas();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Nenhuma consulta selecionada", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
                } 
            }
            else
                JOptionPane.showMessageDialog(null, "O paciente não possui consultas que podem ser alteradas", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        });
        
    }
    
    private void atualizaTabelaConsultas() {
        //Gera a tabela de consultas e coloca essa tabela na tabela da view
        view.getTabelaConsulta().setModel(geraTabelaConsultas());
    }
    
    private DefaultTableModel geraTabelaConsultas() {
        //Somente as consultas que ainda não ocorreram podem ser atualizadas. Para isso, quando a classe AtualizaConsultaController é criada, é passado uma lista
        //com todas as consultas do paciente como parâmetro. Essa lista é obtida através do CPF do paciente que se deseja atualizar a consulta.
        //Através dessa lista de consultas, é gerada uma tabela com as informações que serão exibidas ao usuário
        Object[][] consultas = new Object[this.consultas.size()][Consulta.class.getDeclaredFields().length];
        //Como só as consultas que ainda não ocorreram podem ser atualizadas, é obtido a data atual
        Date d = Date.valueOf(LocalDate.now());
        int i = 0;
        int j = 0;
        //É realizada uma verificação da data de todas as consultas da lista. Caso a consulta possua data posterior à data atual, 
        //significa que ela ainda não ocorreu, logo ela é adicionada à tabela de consultas disponíveis para atualização
        for (Consulta c : this.consultas) {
            if(!c.getDataConsulta().before(d)){
               for (Field field : c.getClass().getDeclaredFields()) {
                try {
                    consultas[i][j] = PropertyUtils.getSimpleProperty(c, field.getName());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    consultas[i][j] = "";
                }
                j++;
            }
            Paciente p = (Paciente) consultas[i][4];
            String nome = p.getNome();
            consultas[i][4] = nome;
            j = 0;
            i++; 
            }
            
        }
        return new javax.swing.table.DefaultTableModel(
                consultas, new String[]{
                    "Código", "Data Consulta", "Hora", "Médico", "Paciente", "Tipo"
                });

    }
}
