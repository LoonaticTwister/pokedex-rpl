/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pokedex.ujian.view;

import com.pokedex.ujian.model.Pokemon;
import com.pokedex.ujian.service.PokemonService;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListCellRenderer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author BAYU
 */
@org.springframework.stereotype.Component
public class PokedexView extends javax.swing.JFrame {

    @Autowired
    private PokemonService pokemonService;
    
    private final String[] TYPES = {
        "Normal", "Api", "Air", "Rumput", "Listrik", "Es", "Bertarung",
        "Racun", "Tanah", "Terbang", "Psikis", "Serangga", "Batu",
        "Hantu", "Naga", "Baja", "Peri"
    };
    
    private final Map<String, Color> typeColors = new HashMap<>();

    /**
     * Creates new form PokedexView
     */
    public PokedexView() {
        initComponents();
        initCustomSetup();
    }
    
    private void initCustomSetup() {
        typeColors.put("Normal", new Color(168, 167, 122));
        typeColors.put("Api", new Color(238, 129, 48));
        typeColors.put("Air", new Color(99, 144, 240));
        typeColors.put("Rumput", new Color(122, 199, 76));
        typeColors.put("Listrik", new Color(247, 208, 44));
        typeColors.put("Es", new Color(150, 217, 214));
        typeColors.put("Bertarung", new Color(194, 46, 40));
        typeColors.put("Racun", new Color(163, 62, 161));
        typeColors.put("Tanah", new Color(226, 191, 101));
        typeColors.put("Terbang", new Color(169, 143, 243));
        typeColors.put("Psikis", new Color(249, 85, 135));
        typeColors.put("Serangga", new Color(166, 185, 26));
        typeColors.put("Batu", new Color(182, 161, 54));
        typeColors.put("Hantu", new Color(115, 87, 151));
        typeColors.put("Naga", new Color(111, 53, 252));
        typeColors.put("Baja", new Color(183, 183, 206));
        typeColors.put("Peri", new Color(214, 133, 173));

        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>(TYPES);
        cbTipe1.setModel(model1);
        
        DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>(TYPES);
        model2.insertElementAt("-", 0);
        cbTipe2.setModel(model2);
        cbTipe2.setSelectedIndex(0);

        TypeRenderer renderer = new TypeRenderer();
        cbTipe1.setRenderer(renderer);
        cbTipe2.setRenderer(renderer);
        
        cbTipe1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTypeColor(cbTipe1);
            }
        });
        
        cbTipe2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTypeColor(cbTipe2);
            }
        });
        
        updateTypeColor(cbTipe1);
        updateTypeColor(cbTipe2);

        setupAutocomplete();
        
        refreshTable();
        
        tblPokemon.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblPokemon.getSelectedRow() != -1) {
                    fillFormFromSelection();
                }
            }
        });
        
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                refreshTable();
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePokemon();
            }
        });
        
        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePokemon();
            }
        });
    }

    private void updateTypeColor(JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if (selected != null && typeColors.containsKey(selected)) {
            comboBox.setBackground(typeColors.get(selected));
        } else {
            comboBox.setBackground(Color.WHITE);
        }
    }
    
    class TypeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            String type = (String) value;
            if (type != null && typeColors.containsKey(type)) {
                setBackground(typeColors.get(type));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }
    
    private void setupAutocomplete() {
        JPopupMenu popup = new JPopupMenu();
        txtEvolusi.setLayout(new java.awt.BorderLayout());
        
        txtEvolusi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = txtEvolusi.getText().trim();
                if (text.length() < 2) {
                    popup.setVisible(false);
                    return;
                }
                
                if (pokemonService != null) {
                    List<String> suggestions = pokemonService.searchPokemonNames(text);
                    if (!suggestions.isEmpty()) {
                        popup.removeAll();
                        for (String s : suggestions) {
                            JMenuItem item = new JMenuItem(s);
                            item.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    txtEvolusi.setText(s);
                                    popup.setVisible(false);
                                }
                            });
                            popup.add(item);
                        }
                        popup.show(txtEvolusi, 0, txtEvolusi.getHeight());
                        txtEvolusi.requestFocus();
                    } else {
                        popup.setVisible(false);
                    }
                }
            }
        });
    }

    public void refreshTable() {
        if (pokemonService == null) return;
        
        List<Pokemon> list = pokemonService.getAllPokemon();
        String[] columns = {"ID", "Nama", "Deskripsi", "Type", "Evolusi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Pokemon p : list) {
            String tipeGabung = p.getTipe1();
            if (p.getTipe2() != null && !p.getTipe2().equals("-") && !p.getTipe2().isEmpty()) {
                tipeGabung = tipeGabung + ", " + p.getTipe2();
            }
            
            Object[] rowData = {
                p.getId(),
                p.getNama(),
                p.getDeskripsi(),
                tipeGabung,
                p.getEvolusi()
            };
            model.addRow(rowData);
        }
        tblPokemon.setModel(model);
    }
    
    private void fillFormFromSelection() {
        int row = tblPokemon.getSelectedRow();
        if (row == -1) return;
        
        Long id = (Long) tblPokemon.getValueAt(row, 0);
        
        Pokemon ketemu = null;
        List<Pokemon> semuaData = pokemonService.getAllPokemon();
        for (Pokemon p : semuaData) {
            if (p.getId().equals(id)) {
                ketemu = p;
                break;
            }
        }
        
        if (ketemu != null) {
            txtNama.setText(ketemu.getNama());
            taDeskripsi.setText(ketemu.getDeskripsi());
            cbTipe1.setSelectedItem(ketemu.getTipe1());
            
            if (ketemu.getTipe2() == null) {
                cbTipe2.setSelectedItem("-");
            } else {
                cbTipe2.setSelectedItem(ketemu.getTipe2());
            }
            
            txtEvolusi.setText(ketemu.getEvolusi());
            
            updateTypeColor(cbTipe1);
            updateTypeColor(cbTipe2);
        }
    }
    
    private void clearForm() {
        txtNama.setText("");
        taDeskripsi.setText("");
        cbTipe1.setSelectedIndex(0);
        cbTipe2.setSelectedIndex(0);
        txtEvolusi.setText("");
        tblPokemon.clearSelection();
        updateTypeColor(cbTipe1);
        updateTypeColor(cbTipe2);
    }
    
    private void deletePokemon() {
        int row = tblPokemon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }
        
        Long id = (Long) tblPokemon.getValueAt(row, 0);
        Pokemon p = new Pokemon(); 
        p.setId(id);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus?");
        if (confirm == JOptionPane.YES_OPTION) {
            pokemonService.deletePokemon(p);
            refreshTable();
            clearForm();
        }
    }
    
    private void updatePokemon() {
        int row = tblPokemon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }
        
        try {
            Long id = (Long) tblPokemon.getValueAt(row, 0);

            Pokemon p = new Pokemon();
            p.setId(id);
            p.setNama(txtNama.getText());
            p.setDeskripsi(taDeskripsi.getText());
            p.setTipe1((String) cbTipe1.getSelectedItem());

            String t2 = (String) cbTipe2.getSelectedItem();
            if (t2.equals("-")) {
                p.setTipe2(null);
            } else {
                p.setTipe2(t2);
            }

            p.setEvolusi(txtEvolusi.getText());

            pokemonService.updatePokemon(p);
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            refreshTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPokemon = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbTipe1 = new javax.swing.JComboBox<>();
        cbTipe2 = new javax.swing.JComboBox<>();
        txtEvolusi = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("POKEDEX");

        tblPokemon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama", "Deskripsi", "Type"
            }
        ));
        jScrollPane1.setViewportView(tblPokemon);

        btnRefresh.setText("Refresh");

        btnHapus.setText("Hapus");

        btnUbah.setText("Ubah");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama");

        jLabel3.setText("Deskripsi");

        taDeskripsi.setColumns(20);
        taDeskripsi.setRows(5);
        jScrollPane2.setViewportView(taDeskripsi);

        jLabel4.setText("Tipe 1");

        jLabel5.setText("Tipe 2");

        jLabel6.setText("Evolusi");

        cbTipe2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(103, 103, 103)
                                .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(99, 99, 99)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(99, 99, 99)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbTipe2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEvolusi, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbTipe1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(cbTipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cbTipe2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtEvolusi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        try {
            Pokemon p = new Pokemon();
            p.setNama(txtNama.getText());
            p.setDeskripsi(taDeskripsi.getText());
            p.setTipe1((String) cbTipe1.getSelectedItem());
            
            String t2 = (String) cbTipe2.getSelectedItem();
            if (t2.equals("-")) {
                p.setTipe2(null);
            } else {
                p.setTipe2(t2);
            }
            
            p.setEvolusi(txtEvolusi.getText());
            
            pokemonService.savePokemon(p);
            JOptionPane.showMessageDialog(this, "Berhasil ditambahkan!");
            refreshTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PokedexView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PokedexView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PokedexView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PokedexView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PokedexView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cbTipe1;
    private javax.swing.JComboBox<String> cbTipe2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTable tblPokemon;
    private javax.swing.JTextField txtEvolusi;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
