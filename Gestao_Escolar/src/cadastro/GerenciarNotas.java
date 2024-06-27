package cadastro;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class GerenciarNotas extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfPesquisa;
	private JButton btnPesquisar, btnAtualizar, btnAdicionarAtualizarNotas, btnVoltar;
	private JTable table;
	private DefaultTableModel tableModel;

	private static final String URL = "jdbc:mysql://localhost:3306/cadastro_alunos";
	private static final String USER = "root";
	private static final String PASSWORD = "G1u2i3l4***";

	public GerenciarNotas() {
		setTitle("Gerenciar Notas");
		setSize(970, 650);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new BorderLayout());

		// Painel superior para pesquisa
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Pesquisar por Matrícula ou CPF:"));
		tfPesquisa = new JTextField(20);
		topPanel.add(tfPesquisa);
		btnPesquisar = new JButton("Pesquisar");
		topPanel.add(btnPesquisar);
		btnAtualizar = new JButton("Atualizar");
		topPanel.add(btnAtualizar);

		btnPesquisar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pesquisarAluno();
			}
		});

		btnAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				carregarNotas();
			}
		});

		panel.add(topPanel, BorderLayout.NORTH);

		// Tabela para exibir as notas dos alunos
		tableModel = new DefaultTableModel(new String[] { "Matrícula", "CPF", "Nome", "Nota 1", "Nota 2", "Nota 3",
				"Nota 4", "Frequência", "Situação" }, 0);
		table = new JTable(tableModel);
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Desabilita o redimensionamento automático

		// Centralizar texto nas células
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			columnModel.getColumn(i).setCellRenderer(centerRenderer);
		}

		columnModel.getColumn(0).setPreferredWidth(80); // Largura da coluna Matrícula
		columnModel.getColumn(1).setPreferredWidth(120); // Largura da coluna CPF
		for (int i = 2; i < columnModel.getColumnCount() - 1; i++) {
			columnModel.getColumn(i).setPreferredWidth(100); // Largura das demais colunas
		}
		columnModel.getColumn(8).setPreferredWidth(150); // Largura da coluna Situação

		// Painel inferior para ações
		JPanel bottomPanel = new JPanel();
		btnAdicionarAtualizarNotas = new JButton("Adicionar/Atualizar Notas");
		bottomPanel.add(btnAdicionarAtualizarNotas);

		btnVoltar = new JButton("Voltar");
		bottomPanel.add(btnVoltar);

		panel.add(bottomPanel, BorderLayout.SOUTH);

		getContentPane().add(panel);

		btnAdicionarAtualizarNotas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirTelaAdicionarAtualizarNotas();
				recarregarTabela(); // Adicione esta linha
			}
		});

		btnVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		carregarNotas();
	}

	private void carregarNotas() {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "SELECT alunos.matricula, alunos.cpf, alunos.nome, notas.nota1, notas.nota2, notas.nota3, notas.nota4, notas.frequencia, notas.situacao FROM alunos LEFT JOIN notas ON alunos.matricula = notas.matricula";
			try (Statement stmt = conn.createStatement()) {
				ResultSet rs = stmt.executeQuery(sql);
				tableModel.setRowCount(0);
				while (rs.next()) {
					String matricula = rs.getString("matricula");
					String cpf = rs.getString("cpf");
					String nome = rs.getString("nome");
					String nota1 = rs.getString("nota1");
					String nota2 = rs.getString("nota2");
					String nota3 = rs.getString("nota3");
					String nota4 = rs.getString("nota4");
					String frequencia = rs.getString("frequencia");
					String situacao = rs.getString("situacao");
					tableModel.addRow(
							new Object[] { matricula, cpf, nome, nota1, nota2, nota3, nota4, frequencia, situacao });
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + e.getMessage());
		}
	}

	private void pesquisarAluno() {
		String pesquisa = tfPesquisa.getText().trim();
		if (pesquisa.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, insira uma matrícula ou CPF para pesquisar.");
			return;
		}
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "SELECT alunos.matricula, alunos.cpf, alunos.nome, notas.nota1, notas.nota2, notas.nota3, notas.nota4, notas.frequencia, notas.situacao FROM alunos LEFT JOIN notas ON alunos.matricula = notas.matricula WHERE alunos.matricula = ? OR alunos.cpf = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, pesquisa);
				stmt.setString(2, pesquisa);
				ResultSet rs = stmt.executeQuery();
				tableModel.setRowCount(0);
				if (rs.next()) {
					String matricula = rs.getString("matricula");
					String cpf = rs.getString("cpf");
					String nome = rs.getString("nome");
					String nota1 = rs.getString("nota1");
					String nota2 = rs.getString("nota2");
					String nota3 = rs.getString("nota3");
					String nota4 = rs.getString("nota4");
					String frequencia = rs.getString("frequencia");
					String situacao = rs.getString("situacao");
					tableModel.addRow(
							new Object[] { matricula, cpf, nome, nota1, nota2, nota3, nota4, frequencia, situacao });
				} else {
					JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao pesquisar aluno: " + e.getMessage());
		}
	}

	private void abrirTelaAdicionarAtualizarNotas() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para atualizar as notas.");
			return;
		}
		String matricula = getStringOrEmpty(tableModel.getValueAt(selectedRow, 0));
		String cpf = getStringOrEmpty(tableModel.getValueAt(selectedRow, 1));
		String nome = getStringOrEmpty(tableModel.getValueAt(selectedRow, 2));
		String nota1 = getStringOrEmpty(tableModel.getValueAt(selectedRow, 3));
		String nota2 = getStringOrEmpty(tableModel.getValueAt(selectedRow, 4));
		String nota3 = getStringOrEmpty(tableModel.getValueAt(selectedRow, 5));
		String nota4 = getStringOrEmpty(tableModel.getValueAt(selectedRow, 6));
		String frequencia = getStringOrEmpty(tableModel.getValueAt(selectedRow, 7));
		new AdicionarNotas(this, new String[] { matricula, cpf, nome, nota1, nota2, nota3, nota4, frequencia })
		.setVisible(true);
	}

	private void recarregarTabela() {
		carregarNotas();
	}

	private String getStringOrEmpty(Object value) {
		return value != null ? value.toString() : "";
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GerenciarNotas().setVisible(true);
			}
		});
	}
}
