package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Encontro {
    private int idEncontro;
    private int idMentoria;
    private LocalDate data;
    private LocalTime horario;
    private String tipoEncontro, descricao, linkReuniao;

    public Encontro() {}

	public int getIdEncontro() {
		return idEncontro;
	}

	public void setIdEncontro(int idEncontro) {
		this.idEncontro = idEncontro;
	}

	public int getIdMentoria() {
		return idMentoria;
	}

	public void setIdMentoria(int idMentoria) {
		this.idMentoria = idMentoria;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public String getTipoEncontro() {
		return tipoEncontro;
	}

	public void setTipoEncontro(String tipoEncontro) {
		this.tipoEncontro = tipoEncontro;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLinkReuniao() {
		return linkReuniao;
	}

	public void setLinkReuniao(String linkReuniao) {
		this.linkReuniao = linkReuniao;
	}
    
    
}
