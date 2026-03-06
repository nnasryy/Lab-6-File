/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;
import org.apache.poi.xwpf.usermodel.*;
/**
 *
 * @author nasry
 */

public class TablaManager {

    public void crearTabla(XWPFDocument doc, int filas, int columnas) {

        XWPFTable tabla = doc.createTable(filas, columnas);

        for (int i = 0; i < filas; i++) {

            XWPFTableRow row = tabla.getRow(i);

            for (int j = 0; j < columnas; j++) {

                row.getCell(j).setText(" ");

            }
        }
    }
}
