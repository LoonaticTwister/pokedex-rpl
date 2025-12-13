package com.pokedex.ujian.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "deskripsi", length = 500)
    private String deskripsi;

    @Column(name = "tipe1", nullable = false)
    private String tipe1;

    @Column(name = "tipe2")
    private String tipe2;

    @Column(name = "evolusi")
    private String evolusi;

    public Pokemon() {
    }

    public Pokemon(String nama, String deskripsi, String tipe1, String tipe2, String evolusi) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.tipe1 = tipe1;
        this.tipe2 = tipe2;
        this.evolusi = evolusi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTipe1() {
        return tipe1;
    }

    public void setTipe1(String tipe1) {
        this.tipe1 = tipe1;
    }

    public String getTipe2() {
        return tipe2;
    }

    public void setTipe2(String tipe2) {
        this.tipe2 = tipe2;
    }

    public String getEvolusi() {
        return evolusi;
    }

    public void setEvolusi(String evolusi) {
        this.evolusi = evolusi;
    }
}
