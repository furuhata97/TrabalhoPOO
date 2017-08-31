/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Consulta;
import model.Secretaria;
import util.GeraDatas;
import view.DetalhesConsultaView;

/**
 *
 * @author gusta
 */
public class DetalhesConsultaController {
    private Consulta consulta;
    private DetalhesConsultaView view;
    private int tipo;
    private Secretaria secretaria;
    
    /* Contrutor padrão */
    public DetalhesConsultaController() {
    }

    /*Contrutor parametrizado.*/
    public DetalhesConsultaController(DetalhesConsultaView view, Consulta consulta) {
        this.consulta = consulta;
        this.view = view;
        view.getDataInvalida().setVisible(false);
        view.getSemHorario().setVisible(false);
        view.getNomeMedico().setVisible(false);
    }
    
    public DetalhesConsultaController(DetalhesConsultaView view, Consulta consulta, int tipo, Secretaria secretaria) {
        this.consulta = consulta;
        this.view = view;
        this.tipo = tipo;
        this.secretaria = secretaria;
        view.getDataInvalida().setVisible(false);
        view.getSemHorario().setVisible(false);
        view.getNomeMedico().setVisible(false);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Seta a view com as informações do paciente
        view.getNomePaciente().setText(consulta.getPaciente().getNome());
        view.getDataConsulta().setText(consulta.getDataConsulta().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getMedico().setText(consulta.getMedico());
        view.getCodigo().setText(consulta.getCodigo().toString());
        //O tipo de consulta é uma comboBox na view, por isso é verificado a String de tipo de consulta da consulta, deixando a comboBox com o tipo correto já selecionado
        if(consulta.getTipoConsulta().equals("Retorno")){
            view.getTipoConsulta().setSelectedIndex(0);
        }
        else
            view.getTipoConsulta().setSelectedIndex(1);
        //Realiza a remoção de todos os itens da comboBox horas e adiciona apenas a hora da consulta
        view.getHora().removeAllItems();
        view.getHora().addItem(consulta.getHora());
        
        //Como a view poderá ser utilizada para alteração, cancelamento e busca de consultas, os campos de nome e código são setados para não edição, pos
        //não deve ser possível a alteração do código e nome do paciente da consulta
        view.getCodigo().setEditable(false);
        view.getNomePaciente().setEditable(false);
        
        //Para saber se a view está sendo utilizada para alteração, cancelamento e busca de consultas, é passado no construtor um inteiro chamado tipo
        //que diferencia o tipo de ação. Caso o inteiro "tipo" seja igual à 2 ou 3 significa que a view será utilizada para cancelamento e busca de consultas,
        //logo os campos da view não podem ser modificados. Assim, todos os campos da view são marcados como não editáveis. Caso o "tipo" seja 1, os campos
        //ficam disponíveis para edição
        if(this.tipo == 2 || this.tipo == 3){
            view.getHora().setEnabled(false);
            view.getDataConsulta().setEditable(false);
            view.getMedico().setEditable(false);
            view.getTipoConsulta().setEnabled(false);
        }
        
        //Caso a view seja utilizada apenas para busca de consulta, o botão Cancelar é ocultado
        if(this.tipo == 2){
            view.getBtnCancelar().setVisible(false);
        }
        
        //Caso o campo médico esteja disponível para edição, é feita uma verificação quando o foco deste campo é perdido. Caso o campo fique em branco, uma mensagem
        //ficará visível ao usuário, alertando-o de que o campo de médico não deve ficar em branco. Assim que o campo é preenchido a mensagem é ocultada
        view.getMedico().addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e){
                String medico = view.getMedico().getText();
                if(medico.equals("")){
                    view.getNomeMedico().setVisible(true);
                }
                else
                    view.getNomeMedico().setVisible(false);
            }
        });
        
        //Caso o campo data esteja disponível para edição, é feita uma verificação quando o foco deste campo é perdido. Caso o campo fique em branco, possua uma 
        //data anterior à data atual ou possua formato inválido, uma mensagem ficará visível ao usuário, alertando-o de que o campo 
        //de data foi preenchido de forma errada. Assim que uma data válida é preenchida a mensagem é ocultada
        view.getDataConsulta().addFocusListener(new FocusAdapter() {  
            @Override  
            public void focusLost(FocusEvent e) {
                try {
                    Date data = GeraDatas.geraDataString(view.getDataConsulta().getText());
                    Date d = Date.valueOf(LocalDate.now());
                    if(data.before(d)){
                        view.getDataInvalida().setVisible(true);
                        return; 
                    }
                } catch (ParseException ex) {
                    view.getDataInvalida().setVisible(true);
                    return;
                }
                view.getDataInvalida().setVisible(false);
            }  
        });
        
        //Caso o campo hora esteja disponível para edição, é feita uma verificação quando este campo ganha foco. Caso os campos de médico e data estejam preenchidos de
        //forma errada, uma mensagem ficará visível ao usuário, alertando-o de que não há datas disponíveis e a comboBox ficará vazia.
        //Caso os campos médico e data estejam corretos, a mensagem será ocultada e a comboBox será preenchida com todos os horários disponíveis na data solicitada com
        //o médico solicitado
        view.getHora().addFocusListener(new FocusAdapter() {  
        @Override  
        public void focusGained(FocusEvent e) {
            view.getSemHorario().setVisible(false);
            try {
                Date data = GeraDatas.geraDataString(view.getDataConsulta().getText());
                ArrayList<LocalTime> horas = horariosDisponiveis(data, view.getTipoConsulta().getSelectedItem().toString(), view.getMedico().getText());
                view.getHora().removeAllItems();
                if(horas.isEmpty() || horas == null)
                    view.getSemHorario().setVisible(true);
                else{
                    for(int i = 0; i<horas.size(); i++){
                        view.getHora().addItem(horas.get(i).toString());
                    }
                }
                view.getHora().setMaximumRowCount(5);
            } catch (ParseException | NullPointerException ex) {
                //Logger.getLogger(NovaConsultaController.class.getName()).log(Level.SEVERE, null, ex);
                view.getSemHorario().setVisible(true);
            }  
        }  
        });
        
        //Quando o botão Ok é clicado, é realizado a ação de acordo com o "tipo"
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            
            //Caso seja do "tipo" 2, significa que foi realizada uma busca de consulta e seus detalhes foram exibidos, logo, ao clicar no botão OK, a view apenas fechará
            if(this.tipo == 2)
                view.dispose();
            
            //Caso seja do "tipo" 1, significa que será feita uma atualização da consulta ao clicar no botão Ok. Quando o botão for clicado, será verificada as informações
            //que foram preenchidas. Caso a data esteja incorreta e/ou médico esteja em branco, não será permitida a atualização da consulta enquanto estes dois campos não
            //forem preenchidos corretamente. Caso os dois dados estejam corretos, será feita uma tentativa de atualização. Caso funcione, a consulta é atualizada,
            //uma mensagem de sucesso é exibida ao usuário e a view é fechada
            if(this.tipo == 1){
              try {
               Date dataConsulta = GeraDatas.geraDataString(view.getDataConsulta().getText());
               Date d = Date.valueOf(LocalDate.now());
                    if(dataConsulta.before(d)){
                        JOptionPane.showMessageDialog(view, "Não é possível agendar uma consulta em uma data anterior a hoje", "Erro", JOptionPane.WARNING_MESSAGE);
                        return; 
                    }
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(view, "A data não possui padrão dd/mm/aaaa!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String medico = view.getMedico().getText();
            if(medico.equals("")){
                JOptionPane.showMessageDialog(view, "Digite o médico responsável pela consulta", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                preencheCampos();
            } catch (ParseException ex) {
                Logger.getLogger(RealizaAtualizacaoPacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(view, "Consulta atualizada com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();  
            }
            
            //Caso seja do "tipo" 3, significa que está ocorrendo uma remoção de consulta. Logo, quando o botão Ok for clicado, será chamado o método para remoçào de
            //consultas, sendo exibida uma mensagem de sucesso ao usuário e a view é fechada
            if(this.tipo == 3){
                secretaria.removeConsulta(consulta);
                JOptionPane.showMessageDialog(view, "Consulta removida com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            }
        });
        
        //Caso o botão cancelar seja clicado (vísivel apenas para atualização e cancelamento de consultas), a view é fechada
        view.getBtnCancelar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
        });
        this.view.setVisible(true);
    }
    
    
    public ArrayList<LocalTime> horariosDisponiveis(Date data, String tipo, String medico){
        //Este método devolve uma lista com todos os horários disponíveis, na data solicitada e com o tipo e médico desejado
        
        //Este array list recebe uma lista com todos os horários possíveis em que qualquer consulta pode ser marcada. As horas vão das 8:00 da manhã até às 17:30
        ArrayList<LocalTime> horas = listaTodosHorarios();
        
        //Recebe todas as consultas cadastradas
        List<Consulta> listaConsultas = secretaria.listaConsultas();
        
        //Caso o médico passado como parâmetro esteja em branco, significa que não pode haver horários para consultas disponíveis sem um médico, logo é retornado o valor null
        if(medico.equals(""))
            return null;
        //Caso o médico não esteja em branco, é feita uma comparação para cada consulta que está em listaConsultas.
        //Caso a data da consulta seja igual a data passada por parâmetro e o médico da consulta seja igual ao médico passado por parâmetro, significa que não
        //pode haver uma nova consulta nesse horário, logo esse horário é removido das horas disponíveis. É verificado então o tipo da Consulta
        //Caso o tipo seja Normal, o próximo horário disponível também é removido, pois a consulta normal leva uma hora, e cada horário possui duração de 
        //30 minutos
        for(int i = 0; i<listaConsultas.size(); i++){
            if(listaConsultas.get(i).getDataConsulta().compareTo(data)==0 && medico.equals(listaConsultas.get(i).getMedico())){
                System.out.println(data);
                if(listaConsultas.get(i).getTipoConsulta().equals("Normal")){
                    LocalTime local = LocalTime.parse(listaConsultas.get(i).getHora());
                    horas.remove(local);
                    try{
                        LocalTime local2 = LocalTime.parse(listaConsultas.get(i+1).getHora());
                        horas.remove(local2);
                    }catch(IndexOutOfBoundsException iou){
                        System.out.println("Fim das consultas");
                    } 
                }
                else{
                    LocalTime local = LocalTime.parse(listaConsultas.get(i).getHora());
                    System.out.println(local);
                    horas.remove(local);
                }
            }   
        }
        //caso o tipo de consulta seja normal, significa que a consulta tem duração de 1 hora, logo a consulta utilizará 2 horários
        if (tipo.equals("Normal")){
            LocalTime cal;
            ArrayList<LocalTime> maisHorasPraExcluir = new ArrayList<>();
            maisHorasPraExcluir.add(horas.get(horas.size()-1));
            //é feita a verificação do próximo horário disponível. Caso o próximo horário seja 30 minutos maior, significa que os 2 horários podem ser selecionados 
            //para consulta do tipo Normal. Caso o próximo horário seja 1 hora maior, significa que o próximo horário ao atual já está ocupado e a consulta 
            //não pode ser marcada neste horário. Exemplo: há 1 horário disponível às 15:00 e o próximo disponível é às 16 horas. Nesse caso a consulta de tipo 
            //Normal não pode ser marcada às 15:00 pois o horário das 15:30 já está ocupado
            for (int i = 0; i<horas.size()-1; i++){
                if(horas.get(i).getMinute() == 30)
                    cal = LocalTime.of(horas.get(i).getHour()+1, 0, 0);
                else
                    cal = LocalTime.of(horas.get(i).getHour(), 30, 0);
                if (!horas.get(i+1).equals(cal)){
                    maisHorasPraExcluir.add(horas.get(i));
                }
            }
            for(int i = 0; i<maisHorasPraExcluir.size(); i++){
                horas.remove(maisHorasPraExcluir.get(i));
            }
        }
        return horas;
    }
    
    public ArrayList<LocalTime> listaTodosHorarios(){
        ArrayList<LocalTime> listaHoras = new ArrayList<>();
        LocalTime l;
        //começa em 8 pois foi definido que o primeiro horário de consulta na clínica começa às 8 e vai até 17 pos foi definido que o último horário de consulta é às 17:30
        for (int i = 8; i<=17; i++){
            l = LocalTime.of(i, 0, 0);
            //adiciona a hora definida na lista de horários disponíveis
            listaHoras.add(l);
            l = LocalTime.of(i, 30, 0);
            //adiciona o horário na lista de horários disponíveis
            listaHoras.add(l);
        }
        return listaHoras;
    }
    
    private void preencheCampos() throws ParseException {
        //Método que preenche os campos da consulta com as informções que deverão ser atualizadas e chama o método adicionaConsulta que faz a atualização da consulta
        //no banco de dados
        consulta.setMedico(view.getMedico().getText());
        consulta.setDataConsulta(GeraDatas.geraDataString(view.getDataConsulta().getText()));
        consulta.setHora(view.getHora().getItemAt(view.getHora().getSelectedIndex()));
        consulta.setTipoConsulta(view.getTipoConsulta().getItemAt(view.getTipoConsulta().getSelectedIndex()));
        secretaria.adicionaConsulta(consulta);
    }
}
