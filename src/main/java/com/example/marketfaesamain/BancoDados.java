package com.example.marketfaesamain;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDados {

    // Caminho fixo: salva o arquivo na pasta do projeto (diretório de trabalho)
    // System.getProperty("user.dir") retorna sempre o diretório raiz do projeto no IntelliJ
    private static final String ARQUIVO = System.getProperty("user.dir")
            + File.separator + "servicos.dat";

    public static void salvar(List<Servico> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO))) {

            oos.writeObject(new ArrayList<>(lista));

            // Imprime o caminho no console para você confirmar onde está sendo salvo
            System.out.println("Dados salvos em: " + ARQUIVO);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Servico> carregar() {
        File arquivo = new File(ARQUIVO);

        System.out.println("Procurando dados em: " + ARQUIVO);

        if (!arquivo.exists()) {
            System.out.println("Arquivo nao encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {

            List<Servico> lista = (List<Servico>) ois.readObject();
            System.out.println("Dados carregados: " + lista.size() + " servico(s).");
            return lista;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}