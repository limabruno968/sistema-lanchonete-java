package lanchonete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe que representa um item do cardápio
class ItemCardapio {
    int codigo;
    String nome;
    double preco;

    public ItemCardapio(int codigo, String nome, double preco) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome + " (R$ " + String.format("%.2f", preco) + ")";
    }
}

// Classe para registrar as vendas concluídas
class Venda {
    String horario;
    double valorTotal;
    String formaPagamento;

    public Venda(double valorTotal, String formaPagamento) {
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.horario = sdf.format(new Date());
    }
}

public class Main {
    private static List<ItemCardapio> cardapio = new ArrayList<>();
    private static List<ItemCardapio> carrinho = new ArrayList<>();
    private static List<Venda> historicoVendas = new ArrayList<>();

    public static void main(String[] args) {
        // Inicializando o cardápio
        cardapio.add(new ItemCardapio(1, "X-Burger", 15.00));
        cardapio.add(new ItemCardapio(2, "X-Salada", 18.50));
        cardapio.add(new ItemCardapio(3, "X-Bacon", 22.00));
        cardapio.add(new ItemCardapio(4, "Batata Frita (M)", 10.00));
        cardapio.add(new ItemCardapio(5, "Refrigerante 350ml", 6.00));
        cardapio.add(new ItemCardapio(6, "Suco Natural", 8.00));
        cardapio.add(new ItemCardapio(7, "Milkshake", 12.00));

        // Configurando a janela principal (JFrame)
        JFrame janela = new JFrame("Lanchonete Java System");
        janela.setSize(480, 480);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());

        // Título no topo
        JLabel titulo = new JLabel("BEM-VINDO À LANCHONETE", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        janela.add(titulo, BorderLayout.NORTH);

        // Painel central com os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(4, 1, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton btnCardapio = new JButton("Ver Cardápio / Fazer Pedido");
        JButton btnCarrinho = new JButton("Ver Carrinho / Fechar Conta");
        JButton btnRelatorio = new JButton("Relatório de Vendas do Dia");
        JButton btnSair = new JButton("Sair");

        // Estilizando os botões
        Font fonteBotoes = new Font("Arial", Font.PLAIN, 14);
        btnCardapio.setFont(fonteBotoes);
        btnCarrinho.setFont(fonteBotoes);
        btnRelatorio.setFont(fonteBotoes);
        btnSair.setFont(fonteBotoes);

        painelBotoes.add(btnCardapio);
        painelBotoes.add(btnCarrinho);
        painelBotoes.add(btnRelatorio);
        painelBotoes.add(btnSair);

        janela.add(painelBotoes, BorderLayout.CENTER);

        // Ação do botão: Ver Cardápio e Adicionar
        btnCardapio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemCardapio[] itensArray = cardapio.toArray(new ItemCardapio[0]);
                ItemCardapio escolhido = (ItemCardapio) JOptionPane.showInputDialog(
                        janela,
                        "Escolha um item para adicionar ao carrinho:",
                        "Cardápio",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        itensArray,
                        itensArray[0]
                );

                if (escolhido != null) {
                    carrinho.add(escolhido);
                    JOptionPane.showMessageDialog(janela, escolhido.nome + " adicionado ao carrinho!");
                }
            }
        });

        // Ação do botão: Ver Carrinho e Fechar Conta
        btnCarrinho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carrinho.isEmpty()) {
                    JOptionPane.showMessageDialog(janela, "Seu carrinho está vazio!", "Carrinho", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                StringBuilder resumo = new StringBuilder("ITENS NO CARRINHO:\n\n");
                double total = 0;
                for (int i = 0; i < carrinho.size(); i++) {
                    ItemCardapio item = carrinho.get(i);
                    resumo.append((i + 1)).append(". ").append(item.nome).append(" - R$ ").append(String.format("%.2f", item.preco)).append("\n");
                    total += item.preco;
                }
                resumo.append("\nVALOR TOTAL: R$ ").append(String.format("%.2f", total));

                int opcao = JOptionPane.showConfirmDialog(
                        janela,
                        resumo.toString() + "\n\nDeseja finalizar o pedido?",
                        "Fechar Conta",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcao == JOptionPane.YES_OPTION) {
                    String[] formasPagamento = {"Dinheiro", "Cartão de Crédito/Débito", "Pix"};
                    String pagamento = (String) JOptionPane.showInputDialog(
                        janela,
                        "Escolha a forma de pagamento:",
                        "Pagamento",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        formasPagamento,
                        formasPagamento[0]
                    );

                    if (pagamento != null) {
                        if (pagamento.equals("Dinheiro")) {
                            String valorStr = JOptionPane.showInputDialog(janela, "Total: R$ " + String.format("%.2f", total) + "\nDigite o valor em dinheiro entregue:");
                            if (valorStr != null) {
                                try {
                                    double dinheiro = Double.parseDouble(valorStr.replace(",", "."));
                                    if (dinheiro >= total) {
                                        double troco = dinheiro - total;
                                        JOptionPane.showMessageDialog(janela, "Pagamento aprovado! Troco: R$ " + String.format("%.2f", troco));
                                        
                                        historicoVendas.add(new Venda(total, "Dinheiro"));
                                        carrinho.clear();
                                    } else {
                                        JOptionPane.showMessageDialog(janela, "Valor insuficiente! Pedido cancelado.", "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(janela, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(janela, "Pagamento aprovado com sucesso! Pedido em preparo.");
                            
                            historicoVendas.add(new Venda(total, pagamento));
                            carrinho.clear();
                        }
                    }
                }
            }
        });

        // Ação do botão: Relatório de Vendas do Dia
        btnRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (historicoVendas.isEmpty()) {
                    JOptionPane.showMessageDialog(janela, "Nenhuma venda registrada hoje ainda.", "Relatório de Vendas", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                StringBuilder relatorio = new StringBuilder("=== RELATÓRIO DE VENDAS DO DIA ===\n\n");
                double faturamentoTotal = 0;

                for (int i = 0; i < historicoVendas.size(); i++) {
                    Venda v = historicoVendas.get(i);
                    relatorio.append((i + 1)).append(". Horário: ").append(v.horario)
                             .append("\n   Forma: ").append(v.formaPagamento)
                             .append(" | Valor: R$ ").append(String.format("%.2f", v.valorTotal))
                             .append("\n------------------------------------\n");
                    faturamentoTotal += v.valorTotal;
                }

                relatorio.append("\nTotal de Pedidos Realizados: ").append(historicoVendas.size());
                relatorio.append("\nFaturamento Total: R$ ").append(String.format("%.2f", faturamentoTotal));

                JTextArea textArea = new JTextArea(relatorio.toString(), 12, 30);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);

                JOptionPane.showMessageDialog(
                        janela,
                        scrollPane,
                        "Relatório de Vendas do Dia",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Ação do botão: Sair
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Exibir a janela
        janela.setVisible(true);
    }
}