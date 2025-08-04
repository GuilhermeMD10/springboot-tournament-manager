package mvcexample.business.dto;

import java.util.ArrayList;
import java.util.List;

public class EquipaTitularDtoWithJogadores {
	 private Long id;
	    private String nome;

	    private List<JogadorDto> plantel;

	    //ids dos campeonatos em que participou
	    private List<Long> conquistas;

	    public EquipaTitularDtoWithJogadores(){
	        this.nome = "";
	        this.plantel = new ArrayList<>();
	        this.conquistas = new ArrayList<>();
	    };

	    public EquipaTitularDtoWithJogadores(String nome){
	        this.nome = nome;
	        this.plantel = new ArrayList<>();
	        this.conquistas = new ArrayList<>();
	    };
	    
	    public EquipaTitularDtoWithJogadores(String nome, List<JogadorDto> plantel){
	        this.nome = nome;
	        this.plantel = plantel;
	        this.conquistas = new ArrayList<>();
	    };
	    
	    public Long getId() {
	        return id;
	    }

	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	    public List<JogadorDto> getPlantel() {
	        return plantel;
	    }

	    public void setPlantel(List<JogadorDto> jogadores) {
	        this.plantel = jogadores;
	    }
	    
	    public List<Long> getConquistas() {
	        return conquistas;
	    }

	    public void setConquistas(List<Long> conquistas) {
	        this.conquistas = conquistas;
	    }

	    public void setId(Long id2) {
	        id = id2;
	    }

}
