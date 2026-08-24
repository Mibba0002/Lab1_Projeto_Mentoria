package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Encontro {
    private int idEncontro;
    private int idMentoria;
    private LocalDate data;
    private LocalTime horario;
    private String tipoEncontro, descricao, linkReuniao;
    private String status;
    private String localEncontro;
    private String motivoNaoRealizacao;

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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getLocalEncontro() {
			return localEncontro;
		}

		public void setLocalEncontro(String localEncontro) {
			this.localEncontro = localEncontro;
		}

		public String getMotivoNaoRealizacao() {
			return motivoNaoRealizacao;
		}

		public void setMotivoNaoRealizacao(String motivoNaoRealizacao) {
			this.motivoNaoRealizacao = motivoNaoRealizacao;
		}
    
    
}
