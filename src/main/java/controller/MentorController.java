package controller;

import java.util.ArrayList;

import dao.MentorDao;
import model.Mentor;

public class MentorController {

    private MentorDao mentorDao;

    public MentorController() {
        mentorDao = new MentorDao();
    }

    // CADASTRAR MENTOR

    public boolean cadastrarMentor(Mentor mentor) {

        // Verifica se o CPF já está cadastrado
        if (mentorDao.cpfExiste(mentor.getCpfMentor())) {
            System.out.println("CPF já cadastrado.");
            return false;
        }

        // Verifica se o e-mail já está cadastrado
        if (mentorDao.emailExiste(mentor.getEmail())) {
            System.out.println("E-mail já cadastrado.");
            return false;
        }

        // Cadastra o mentor
        return mentorDao.cadastrar(mentor);
    }


  
    // BUSCAR POR CPF
   
    public Mentor buscarPorCpf(String cpf) {
        return mentorDao.buscarPorCpf(cpf);
    }

    // BUSCAR POR EMAIL
   
    public Mentor buscarPorEmail(String email) {
        return mentorDao.buscarPorCpf(email);
    }


    // LOGIN
    

    public Mentor login(String email, String senha) {
        return mentorDao.login(email, senha);
    }


    //LISTAR MENTORES
   
    public ArrayList<Mentor> listarMentores() {
        return mentorDao.listar();
    }


    // ATUALIZAR MENTOR
 
    public boolean atualizarMentor(Mentor mentor) {
        return mentorDao.atualizar(mentor);
    }


    // EXCLUIR MENTOR
   
    public boolean excluirMentor(String cpf) {
        return mentorDao.excluir(cpf);
    }
}