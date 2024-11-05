/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Casilla;
import Modelo.Tablero;
import Vistas.FrmJuegos;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Dell
 */
public class ventanaController {
    FrmJuegos vista;
    int  NumFilas = 12;
    int  NumColumnas = 12;
    int  NumMinas = 30;
    JButton[][] botonesTablero;
    
    Tablero tableroBmina;
    
    
      void descargarControles(){
        if (botonesTablero!=null){
            for (int i = 0; i < botonesTablero.length; i++) {
                for (int j = 0; j < botonesTablero[i].length; j++) {
                    if (botonesTablero[i][j]!=null){
                        vista.getContentPane().remove(botonesTablero[i][j]);
                    }
                }
            }
        }
    }
    public void juegoNuevo(){
        descargarControles();
        cargarControles();
        crearTableroBuscaminas();
        vista.repaint();
    }

    private void crearTableroBuscaminas(){
        tableroBmina=new Tablero(NumFilas, NumColumnas, NumMinas);
        tableroBmina.setEventoPartidaPerdida(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for(Casilla casillaConMina: t){
                    botonesTablero[casillaConMina.getPosFila()][casillaConMina.getPosColumna()].setText("*");
                }
            for (int i = 0; i < NumFilas; i++) {
                for (int j = 0; j < NumColumnas; j++) {
                    botonesTablero[i][j].setEnabled(false);
                }}
                // Bloquear todos los botones del tablero
            for (int i = 0; i < NumFilas; i++) {
                for (int j = 0; j < NumColumnas; j++) {
                    botonesTablero[i][j].setEnabled(false);
                }
            }

            // Mostrar el JOptionPane de advertencia
            JOptionPane.showMessageDialog(null, 
                "¡Has perdido! Has hecho clic en una mina.", 
                "Fin del juego", 
                JOptionPane.WARNING_MESSAGE);
        }
            
            
        });
        tableroBmina.setEventoPartidaGanada(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for(Casilla casillaConMina: t){
                    botonesTablero[casillaConMina.getPosFila()][casillaConMina.getPosColumna()].setText(":)");
                }
            }
        });
        
        tableroBmina.setEventoCasillaAbierta(new Consumer<Casilla>() {
            @Override
            public void accept(Casilla t) {
                botonesTablero[t.getPosFila()][t.getPosColumna()].setEnabled(false);
                botonesTablero[t.getPosFila()][t.getPosColumna()]
                        .setText(t.getNumMinasAlrededor()==0?"":t.getNumMinasAlrededor()+"");
            }
        });
        tableroBmina.imprimirTablero();
    }
    
   private void cargarControles() {
    int posXReferencia = 25;
    int posYReferencia = 25;
    int anchoControl = 30;
    int altoControl = 30;

    botonesTablero = new JButton[NumFilas][NumColumnas];
    for (int i = 0; i < botonesTablero.length; i++) {
        for (int j = 0; j < botonesTablero[i].length; j++) {
            botonesTablero[i][j] = new JButton();
            botonesTablero[i][j].setName(i + "," + j);
            botonesTablero[i][j].setBorder(null);
            
            if (i == 0 && j == 0) {
                botonesTablero[i][j].setBounds(posXReferencia, posYReferencia, anchoControl, altoControl);
            } else if (i == 0 && j != 0) {
                botonesTablero[i][j].setBounds(
                        botonesTablero[i][j - 1].getX() + botonesTablero[i][j - 1].getWidth(),
                        posYReferencia, anchoControl, altoControl);
            } else {
                botonesTablero[i][j].setBounds(
                        botonesTablero[i - 1][j].getX(),
                        botonesTablero[i - 1][j].getY() + botonesTablero[i - 1][j].getHeight(),
                        anchoControl, altoControl);
            }

            // Agrega el MouseListener para el clic derecho
botonesTablero[i][j].addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        JButton boton = (JButton) e.getSource();
        if (e.getButton() == MouseEvent.BUTTON3) { // Clic derecho
            if (boton.getIcon() == null) {
                boton.setIcon(new ImageIcon(
                        new ImageIcon("C:/Users/Dell/Documents/NetBeansProjects/ProyectoBuscaminas/src/img/bandera-roja.png")
                        .getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
            } else {
                boton.setIcon(null);
            }
        } else if (e.getButton() == MouseEvent.BUTTON1) { // Clic izquierdo
            if (boton.getIcon() != null) {
                // Quitar la imagen antes de revelar el número
                boton.setIcon(null);
            } else {
                // Lógica para mostrar el número u otra acción si no hay icono
                ActionEvent actionEvent = new ActionEvent(boton, ActionEvent.ACTION_PERFORMED, boton.getActionCommand());
                btnClick(actionEvent);
            }
        }
    }
});

            botonesTablero[i][j].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnClick(e);
                }
            });

            vista.getContentPane().add(botonesTablero[i][j]);
        }
    }
    vista.setSize(botonesTablero[NumFilas - 1][NumColumnas - 1].getX() +
            botonesTablero[NumFilas - 1][NumColumnas - 1].getWidth() + 30,
            botonesTablero[NumFilas - 1][NumColumnas - 1].getY() +
            botonesTablero[NumFilas - 1][NumColumnas - 1].getHeight() + 70);
}
    private void btnClick(ActionEvent e) {
        JButton btn=(JButton)e.getSource();
        String[] coordenada=btn.getName().split(",");
        int posFila=Integer.parseInt(coordenada[0]);
        int posColumna=Integer.parseInt(coordenada[1]);
        //JOptionPane.showMessageDialog(rootPane, posFila+","+posColumna);
        tableroBmina.seleccionarCasilla(posFila, posColumna);
        
    }

    public ventanaController(FrmJuegos vista) {
        this.vista = vista;
    }
    
    
}
