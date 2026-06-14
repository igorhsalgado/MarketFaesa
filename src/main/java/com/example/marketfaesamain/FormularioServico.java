package com.example.marketfaesamain;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FormularioServico {

    private TableView<Servico> tabela;
    private ObservableList<Servico> listaServicos;

    public FormularioServico(TableView<Servico> tabela, ObservableList<Servico> listaServicos) {
        this.tabela = tabela;
        this.listaServicos = listaServicos;
    }

    public void abrir(Servico servicoExistente, Stage owner) {
        Stage janela = new Stage();
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.initOwner(owner);
        janela.setTitle(servicoExistente == null ? "Novo Serviço" : "Editar Serviço");

        Label tituloForm = new Label(servicoExistente == null ? "Publicar Serviço" : "Editar Serviço");
        tituloForm.setFont(new Font("Arial", 17));
        tituloForm.setStyle("-fx-font-weight: bold; -fx-text-fill: " + EstiloUI.COR_TEXTO + ";");

        TextField campTitulo    = EstiloUI.criarCampo("Ex: Aula de Cálculo I");
        TextField campDescricao = EstiloUI.criarCampo("Ex: Aulas semanais de 1h para até 3 alunos");
        TextField campValor     = EstiloUI.criarCampo("Ex: 50.00");

        ComboBox<String> campCategoria = EstiloUI.criarComboBox("Selecione uma categoria");
        campCategoria.getItems().addAll("Monitoria", "Programação", "Design", "Idiomas", "Música", "Outro");

        // Campo de categoria personalizada — começa oculto
        // Só aparece quando o usuário escolhe "Outro" no ComboBox
        TextField campCategoriaCustom = EstiloUI.criarCampo("Digite a categoria");
        Label rotuloCustom = EstiloUI.rotulo("Qual categoria?");

        // setVisible(false) oculta visualmente mas mantém o espaço
        // setManaged(false) remove o componente do layout completamente — sem espaço vazio
        campCategoriaCustom.setVisible(false);
        campCategoriaCustom.setManaged(false);
        rotuloCustom.setVisible(false);
        rotuloCustom.setManaged(false);

        // Listener: monitora a seleção do ComboBox
        // Quando "Outro" é escolhido, exibe o campo extra; caso contrário, oculta
        campCategoria.valueProperty().addListener((obs, anterior, atual) -> {
            boolean ehOutro = "Outro".equals(atual);
            campCategoriaCustom.setVisible(ehOutro);
            campCategoriaCustom.setManaged(ehOutro);
            rotuloCustom.setVisible(ehOutro);
            rotuloCustom.setManaged(ehOutro);

            // Limpa o campo customizado se o usuário voltar para outra opção
            if (!ehOutro) campCategoriaCustom.clear();
        });

        // Se for edição, preenche os campos com os dados atuais
        if (servicoExistente != null) {
            campTitulo.setText(servicoExistente.getTitulo());
            campDescricao.setText(servicoExistente.getDescricao());
            campValor.setText(String.valueOf(servicoExistente.getValor()));

            // Verifica se a categoria salva é uma das opções padrão
            // Se não for, significa que foi digitada manualmente via "Outro"
            boolean categoriaEhPadrao = campCategoria.getItems()
                    .contains(servicoExistente.getCategoria());

            if (categoriaEhPadrao) {
                campCategoria.setValue(servicoExistente.getCategoria());
            } else {
                // Seleciona "Outro" e preenche o campo customizado com o valor salvo
                campCategoria.setValue("Outro");
                campCategoriaCustom.setText(servicoExistente.getCategoria());
            }
        }

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: " + EstiloUI.COR_VERMELHO + "; -fx-font-size: 11;");

        Button btnSalvar = EstiloUI.criarBotao("Salvar", EstiloUI.COR_ACENTO, EstiloUI.COR_ACENTO_HOVER);
        btnSalvar.setPrefWidth(Double.MAX_VALUE);

        btnSalvar.setOnAction(e -> {
            String titulo    = campTitulo.getText().trim();
            String selecao   = campCategoria.getValue();
            String descricao = campDescricao.getText().trim();
            String valorTxt  = campValor.getText().trim();

            // Se o usuário escolheu "Outro", usa o texto digitado no campo extra
            // Caso contrário, usa a opção selecionada no ComboBox normalmente
            String categoria;
            if ("Outro".equals(selecao)) {
                categoria = campCategoriaCustom.getText().trim();
                if (categoria.isEmpty()) {
                    lblErro.setText("Digite o nome da categoria personalizada.");
                    return;
                }
            } else {
                categoria = selecao;
            }

            if (titulo.isEmpty() || selecao == null || descricao.isEmpty() || valorTxt.isEmpty()) {
                lblErro.setText("Preencha todos os campos antes de salvar.");
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorTxt.replace(",", "."));
            } catch (NumberFormatException ex) {
                lblErro.setText("O valor deve ser numérico. Ex: 50.00");
                return;
            }

            if (servicoExistente == null) {
                listaServicos.add(new Servico(titulo, categoria, descricao, valor));
            } else {
                servicoExistente.setTitulo(titulo);
                servicoExistente.setCategoria(categoria);
                servicoExistente.setDescricao(descricao);
                servicoExistente.setValor(valor);
                tabela.refresh();
            }

            janela.close();
        });

        // ===== LAYOUT =====
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setPadding(new Insets(25));
        grid.setStyle("-fx-background-color: " + EstiloUI.COR_FUNDO + ";");

        grid.add(tituloForm,                    0, 0, 2, 1);
        grid.add(EstiloUI.rotulo("Título"),     0, 1); grid.add(campTitulo,       1, 1);
        grid.add(EstiloUI.rotulo("Categoria"),  0, 2); grid.add(campCategoria,    1, 2);
        grid.add(rotuloCustom,                  0, 3); grid.add(campCategoriaCustom, 1, 3);
        grid.add(EstiloUI.rotulo("Descrição"),  0, 4); grid.add(campDescricao,    1, 4);
        grid.add(EstiloUI.rotulo("Valor R$"),   0, 5); grid.add(campValor,        1, 5);
        grid.add(lblErro,                       0, 6, 2, 1);
        grid.add(btnSalvar,                     0, 7, 2, 1);

        ColumnConstraints c1 = new ColumnConstraints(80);
        ColumnConstraints c2 = new ColumnConstraints(300);
        grid.getColumnConstraints().addAll(c1, c2);

        janela.setScene(new Scene(grid, 440, 390));
        janela.showAndWait();
    }
}