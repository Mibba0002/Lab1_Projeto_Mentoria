package model;

import java.time.LocalDate;

public class Mentoria {
    private int idMentoria;
    private String cpfMentorado;
    private int idEspecializacao;
    private String status;
    private LocalDate dataInicio, dataFim;
    private String objetivosDefinidos, depoimentos;
    private String cpfMentor;

    public Mentoria() {}

	public int getIdMentoria() {
		return idMentoria;
	}

	public void setIdMentoria(int idMentoria) {
		this.idMentoria = idMentoria;
	}

	public String getCpfMentorado() {
		return cpfMentorado;
	}

	public void setCpfMentorado(String cpfMentorado) {
		this.cpfMentorado = cpfMentorado;
	}

	public int getIdEspecializacao() {
		return idEspecializacao;
	}

	public void setIdEspecializacao(int idEspecializacao) {
		this.idEspecializacao = idEspecializacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public String getObjetivosDefinidos() {
		return objetivosDefinidos;
	}

	public void setObjetivosDefinidos(String objetivosDefinidos) {
		this.objetivosDefinidos = objetivosDefinidos;
	}

	public String getDepoimentos() {
		return depoimentos;
	}

	public void setDepoimentos(String depoimentos) {
		this.depoimentos = depoimentos;
	}

		public String getCpfMentor() {
		return cpfMentor;
	}

	public void setCpfMentor(String cpfMentor) {
		this.cpfMentor = cpfMentor;
	}
    
    
}
