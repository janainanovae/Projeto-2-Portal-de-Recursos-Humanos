// Lançada quando um registro de ponto viola alguma regra de negócio
public class RegistroInvalidoException extends Exception {

    public RegistroInvalidoException(String mensagem) {
        super(mensagem);
    }
}
