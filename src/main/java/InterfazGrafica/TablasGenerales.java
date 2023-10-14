/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazGrafica;

import Analizador.AnalizadorLexico;
import Analizador.ReportesSintacticos;
import Tokens.Simbolos;
import Tokens.TablaSintactica;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author branp
 */
public class TablasGenerales extends javax.swing.JDialog {

    private ReportesSintacticos ReporteSintactico = new ReportesSintacticos();
    private AnalizadorLexico ReporteLexico = new AnalizadorLexico();
    private ArrayList<TablaSintactica> reporteRecopiladoS = new ArrayList<>();
    private ArrayList<Simbolos> reporteRecopiladoL = new ArrayList<>();
    /**
     * Creates new form TablasGenerales
     * @param parent
     * @param modal
     * @param X
     */
    public TablasGenerales(java.awt.Frame parent, boolean modal, int X) {
        super(parent, modal);
        initComponents();
        //Para ponerlo en el centro y que el usuario no lo pueda hacer grande o pequeño
         this.setLocationRelativeTo(null);
         this.setResizable(false);
         int Total;
        DefaultTableModel model = new DefaultTableModel();
        //switch que define el comportamiento de la tabla según el parametro X que se envie
        switch(X){
            //caso 1 para el reporte Lexico:
            case 1:
                reporteRecopiladoL = ReporteLexico.gettokenRecopilado();
                model.addColumn("Token");
                model.addColumn("Patrón ");
                model.addColumn("Lexema");
                model.addColumn("Fila");
                model.addColumn("Columna");
                    for (Simbolos dato : reporteRecopiladoL) {
                    Object[] fila = {dato.getTipoToken(), dato.getPatron(), dato.getLexema(), dato.getFila(), dato.getColumna()};
                    model.addRow(fila);
                }
                Tabla.setModel(model);
                Titulo.setText("Tabla de Simbolos Lexicos");
                break;
                //tabla Global Sintactica
            case 2:    //caso 2 para la tabla Global sintactica   
        reporteRecopiladoS = ReporteSintactico.getreporteRecopilado();
                model.addColumn("Símbolo");
                model.addColumn("Tipo");
                model.addColumn("Valor");
                model.addColumn("Línea");
                model.addColumn("Columna");
                model.addColumn("Bloque");
                    for (TablaSintactica dato : reporteRecopiladoS) {
                    Object[] fila = {dato.getSimbolo(), dato.getTipo(), dato.getValor(), dato.getfila(), dato.getcolumna(),dato.getbloque()};
                    model.addRow(fila);
                }
                Tabla.setModel(model);
                Tabla.getColumnModel().getColumn(2).setPreferredWidth(250);
                Tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
                Tabla.getColumnModel().getColumn(1).setPreferredWidth(70);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(4).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(5).setPreferredWidth(50); 
                Titulo.setText("Tabla de Simbolos Global");
                break;
                //caso 3 para errores lexicos
            case 3:
                reporteRecopiladoL = ReporteLexico.getErroresRecopilado();
                model.addColumn("Token");
                model.addColumn("Error:");
                model.addColumn("Lexema");
                model.addColumn("Fila");
                model.addColumn("Columna");
                    for (Simbolos dato : reporteRecopiladoL) {
                    Object[] fila = {dato.getTipoToken(), dato.getPatron(), dato.getLexema(), dato.getFila(), dato.getColumna()};
                    model.addRow(fila);
                }
                Tabla.setModel(model);
                Titulo.setText("Tabla de Errores Lexicos");
                break;
            case 4:      //Errores sintacticos 
                reporteRecopiladoS = ReporteSintactico.getErrorRecopilado();
                model.addColumn("Tipo");
                model.addColumn("Descripción");
                model.addColumn("Línea");
                model.addColumn("Columna");
                model.addColumn("Bloque");
                    for (TablaSintactica dato : reporteRecopiladoS) {
                    Object[] fila = {dato.getTipo(), dato.getDescripcion(), dato.getfila(), dato.getcolumna(),dato.getbloque()};
                    model.addRow(fila);
                }
                Tabla.setModel(model);    
                Tabla.getColumnModel().getColumn(1).setPreferredWidth(300);
                Tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(2).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(4).setPreferredWidth(50);   
                Titulo.setText("Tabla de Errores Sintacticos");
                break;
            case 5: // Cantidad de funciones o métodos      
                reporteRecopiladoS = ReporteSintactico.getreporteRecopilado();
                Total= 1;
                model.addColumn("No");
                model.addColumn("Metodo");
                model.addColumn("Línea");
                model.addColumn("Columna");
                model.addColumn("Bloque");
                    for (TablaSintactica dato : reporteRecopiladoS) {
                        if(dato.getnivel().equals("Funciones")){
                            Object[] fila = {String.valueOf(Total), dato.getSimbolo(), dato.getfila(), dato.getcolumna(),dato.getbloque()};
                            model.addRow(fila);
                            Total++;
                        }
                }    
                Tabla.setModel(model);    
                Tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
                Tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
                Tabla.getColumnModel().getColumn(2).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50); 
                Titulo.setText("Cantidad de funciones");
                break;
            case 6: // Cantidad de invocaciones de funciones o métodos      
                reporteRecopiladoS = ReporteSintactico.getreporteRecopilado();
                Total = 1;
                model.addColumn("No");
                model.addColumn("Metodo");
                model.addColumn("Línea");
                model.addColumn("Columna");
                model.addColumn("Bloque");
                    for (TablaSintactica dato : reporteRecopiladoS) {
                        if(dato.getnivel().equals("Invocaciones")){
                            Object[] fila = {String.valueOf(Total),dato.getSimbolo(), dato.getfila(), dato.getcolumna(),dato.getbloque()};
                            model.addRow(fila);
                            Total++;
                        }
                }    
                Tabla.setModel(model);
                Tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
                Tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
                Tabla.getColumnModel().getColumn(2).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);   
                Titulo.setText("Cantidad de Invocaciones de funciones");
                break;
            case 7: // Parametros de funciones    
                reporteRecopiladoS = ReporteSintactico.getreporteRecopilado();
                model.addColumn("Metodo");
                model.addColumn("Parametros");
                model.addColumn("Línea");
                model.addColumn("Columna");
                model.addColumn("Bloque");
                    for (TablaSintactica dato : reporteRecopiladoS) {
                        if(dato.getnivel().equals("Funciones")){
                            Object[] fila = {dato.getSimbolo(),dato.getValor(), dato.getfila(), dato.getcolumna(),dato.getbloque()};
                            model.addRow(fila);
                        }
                }    
                Tabla.setModel(model);    
                Tabla.getColumnModel().getColumn(0).setPreferredWidth(250);
                Tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
                Tabla.getColumnModel().getColumn(2).setPreferredWidth(50);
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);  
                Tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
                Titulo.setText("Parametros de funciones:");
                break;  
            default:
                break;
        }
        // Crea un renderizador para centrar el contenido
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                    // Aplica el renderizador a todas las columnas
                    for (int i = 0; i < Tabla.getColumnCount(); i++) {
                        Tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
        Tabla.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));

        Titulo.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setText("Reportes");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Titulo)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JLabel Titulo;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
