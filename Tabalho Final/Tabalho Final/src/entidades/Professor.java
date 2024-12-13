package entidades;

public class Professor {
    private final int id;
    private final String name;
    private final String formacao;

    public Professor(int id, String name, String formacao) {
        this.id = id;
        this.name = name;
        this.formacao = formacao;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormacao() {
        return formacao;
    }

    @Override
    public String toString() {
        return id + ") " + formacao + ". " + name;
    }
}
