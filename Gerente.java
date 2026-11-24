// Gerente: não bate ponto
public class Gerente extends Funcionario {

    public Gerente(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Gerente";
    }

    @Override
    public boolean batePonto() {
        return false;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 0;
    }
}
