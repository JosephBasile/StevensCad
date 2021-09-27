/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author yangbai
 */
public class fileListener implements ActionListener {

   @Override
        public void actionPerformed(ActionEvent e) {
             String ac = e.getActionCommand();
             switch(ac) {
                 case "File": 
                     FileActionPerformed();
                     break;
                 case "Load":
                     LoadActionPerformed();
                     break;
                 case "Export":
                     ExportActionPerformed();
                     break;
                 case "Import":
                     ImportActionPerformed();
                     break;
                 default:
                     break;     
             }
        }
        
        private void FileActionPerformed() {
            
        }
        
        private void LoadActionPerformed() {
            
        }
        
        private void ExportActionPerformed() {
            
        }
        
        private void ImportActionPerformed() {
            
        }
    }
    
