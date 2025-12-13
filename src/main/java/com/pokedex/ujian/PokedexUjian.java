/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.pokedex.ujian;

import com.pokedex.ujian.config.AppConfig;
import com.pokedex.ujian.view.PokedexView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PokedexUjian {

    public static void main(String[] args) {
        // Init Spring Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Run GUI in Event Dispatch Thread
        java.awt.EventQueue.invokeLater(() -> {
            PokedexView view = context.getBean(PokedexView.class);
            view.setVisible(true);
        });
    }
}
