package cadastro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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

public class GerenciarAlunos extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfPesquisa;
	private JButton btnPesquisar, btnAtualizar, btnExcluir, btnVoltar, btnF5;
	private JTable table;
	private DefaultTableModel tableModel;

	private static final String URL = "jdbc:mysql://localhost:3306/cadastro_alunos";
	private static final String USER = "root";
	private static final String PASSWORD = "G1u2i3l4***";

	public GerenciarAlunos() {
		setTitle("Gerenciar Alunos");
		setSize(800, 600);
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
		btnF5 = new JButton("Atualizar");
		topPanel.add(btnF5);

		// Ações dos botões
		btnPesquisar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pesquisarAluno();
			}
		});

		btnF5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				carregarAlunos();
			}
		});

		panel.add(topPanel, BorderLayout.NORTH);

		// Tabela para exibir os alunos
		tableModel = new DefaultTableModel(new String[] { "Matrícula", "Nome", "Data de Nascimento", "Sexo", "CPF",
				"Endereço", "Número", "Bairro", "Cidade", "UF", "CEP", "Estado Civil", "Telefone", "RG",
				"Orgão Emissor", "Email", "Nome da Mãe", "Nome do Pai" }, 0);
		table = new JTable(tableModel);
		// Centralizar conteúdo das células
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		panel.add(new JScrollPane(table), BorderLayout.CENTER);

		// Painel inferior para ações
		JPanel bottomPanel = new JPanel();
		btnAtualizar = new JButton("Atualizar");
		bottomPanel.add(btnAtualizar);

		btnExcluir = new JButton("Excluir");
		bottomPanel.add(btnExcluir);

		btnVoltar = new JButton("Voltar");
		bottomPanel.add(btnVoltar);

		panel.add(bottomPanel, BorderLayout.SOUTH);

		add(panel);

		btnAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirTelaAtualizacao();
			}
		});

		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				excluirAluno();
			}
		});

		btnVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		carregarAlunos();
	}

	private void carregarAlunos() {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "SELECT * FROM alunos";
			try (Statement stmt = conn.createStatement()) {
				ResultSet rs = stmt.executeQuery(sql);
				tableModel.setRowCount(0);
				SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");

				while (rs.next()) {
					String matricula = rs.getString("matricula");
					String nome = rs.getString("nome");
					Date dataNascimento = rs.getDate("data_nascimento");
					String dataNascimentoFormatada = formatoBrasileiro.format(dataNascimento);
					String sexo = rs.getString("sexo");
					String cpf = rs.getString("cpf");
					String endereco = rs.getString("endereco");
					String numero = rs.getString("numero");
					String bairro = rs.getString("bairro");
					String cidade = rs.getString("cidade");
					String uf = rs.getString("uf");
					String cep = rs.getString("cep");
					String estadoCivil = rs.getString("estado_civil");
					String telefone = rs.getString("telefone");
					String rg = rs.getString("rg");
					String orgaoEmissor = rs.getString("orgao_emissor");
					String email = rs.getString("email");
					String nomeMae = rs.getString("nome_mae");
					String nomePai = rs.getString("nome_pai");
					tableModel.addRow(new Object[] { matricula, nome, dataNascimentoFormatada, sexo, cpf, endereco,
							numero, bairro, cidade, uf, cep, estadoCivil, telefone, rg, orgaoEmissor, email, nomeMae,
							nomePai });
				}

				// Ocultar colunas após CPF
				ocultarColunas();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + e.getMessage());
		}
	}

	private void pesquisarAluno() {
		String pesquisa = tfPesquisa.getText().trim();

		if (pesquisa.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, insira uma matrícula ou CPF para pesquisar.");
			return;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "SELECT * FROM alunos WHERE matricula = ? OR cpf = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, pesquisa);
				stmt.setString(2, pesquisa);
				ResultSet rs = stmt.executeQuery();
				tableModel.setRowCount(0);
				if (rs.next()) {
					String matricula = rs.getString("matricula");
					String nome = rs.getString("nome");
					Date dataNascimento = rs.getDate("data_nascimento");
					String dataNascimentoFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataNascimento);
					String sexo = rs.getString("sexo");
					String cpf = rs.getString("cpf");
					String endereco = rs.getString("endereco");
					String numero = rs.getString("numero");
					String bairro = rs.getString("bairro");
					String cidade = rs.getString("cidade");
					String uf = rs.getString("uf");
					String cep = rs.getString("cep");
					String estadoCivil = rs.getString("estado_civil");
					String telefone = rs.getString("telefone");
					String rg = rs.getString("rg");
					String orgaoEmissor = rs.getString("orgao_emissor");
					String email = rs.getString("email");
					String nomeMae = rs.getString("nome_mae");
					String nomePai = rs.getString("nome_pai");
					tableModel.addRow(new Object[] { matricula, nome, dataNascimentoFormatada, sexo, cpf, endereco,
							numero, bairro, cidade, uf, cep, estadoCivil, telefone, rg, orgaoEmissor, email, nomeMae,
							nomePai });
				} else {
					JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
				}

				// Ocultar colunas após CPF
				ocultarColunas();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao pesquisar aluno: " + e.getMessage());
		}
	}

	private void ocultarColunas() {
		// Ocultar colunas após CPF
		for (int i = 5; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setMinWidth(0);
			table.getColumnModel().getColumn(i).setMaxWidth(0);
			table.getColumnModel().getColumn(i).setPreferredWidth(0);
		}
	}

	private void abrirTelaAtualizacao() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para atualizar.");
			return;
		}

		String matricula = tableModel.getValueAt(selectedRow, 0).toString();
		String nome = tableModel.getValueAt(selectedRow, 1).toString();
		String dataNascimento = tableModel.getValueAt(selectedRow, 2).toString();
		String sexo = tableModel.getValueAt(selectedRow, 3).toString();
		String cpf = tableModel.getValueAt(selectedRow, 4).toString();
		String endereco = tableModel.getValueAt(selectedRow, 5).toString();
		String numero = tableModel.getValueAt(selectedRow, 6).toString();
		String bairro = tableModel.getValueAt(selectedRow, 7).toString();
		String cidade = tableModel.getValueAt(selectedRow, 8).toString();
		String uf = tableModel.getValueAt(selectedRow, 9).toString();
		String cep = tableModel.getValueAt(selectedRow, 10).toString();
		String estadoCivil = tableModel.getValueAt(selectedRow, 11).toString();
		String telefone = tableModel.getValueAt(selectedRow, 12).toString();
		String rg = tableModel.getValueAt(selectedRow, 13).toString();
		String orgaoEmissor = tableModel.getValueAt(selectedRow, 14).toString();
		String email = tableModel.getValueAt(selectedRow, 15).toString();
		String nomeMae = tableModel.getValueAt(selectedRow, 16).toString();
		String nomePai = tableModel.getValueAt(selectedRow, 17).toString();

		JDialog dialog = new JDialog(this, "Atualizar Aluno", true);
		dialog.setSize(400, 500);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new GridLayout(20, 2));

		dialog.add(new JLabel("Matrícula:"));
		JTextField tfMatricula = new JTextField(matricula);
		tfMatricula.setEditable(false);
		dialog.add(tfMatricula);

		dialog.add(new JLabel("Nome:"));
		JTextField tfNome = new JTextField(nome);
		dialog.add(tfNome);

		dialog.add(new JLabel("Data de Nascimento:"));
		JTextField tfDataNascimento = new JTextField(dataNascimento);
		dialog.add(tfDataNascimento);

		dialog.add(new JLabel("Sexo:"));
		String[] sexoOptions = { "M", "F" };
		JComboBox<String> cbSexo = new JComboBox<>(sexoOptions);
		cbSexo.setSelectedItem(sexo);
		dialog.add(cbSexo);

		dialog.add(new JLabel("CPF:"));
		JTextField tfCpf = new JTextField(cpf);
		dialog.add(tfCpf);

		dialog.add(new JLabel("Endereço:"));
		JTextField tfEndereco = new JTextField(endereco);
		dialog.add(tfEndereco);

		dialog.add(new JLabel("Número:"));
		JTextField tfNumero = new JTextField(numero);
		dialog.add(tfNumero);

		dialog.add(new JLabel("Bairro:"));
		JTextField tfBairro = new JTextField(bairro);
		dialog.add(tfBairro);

		dialog.add(new JLabel("Cidade:"));
		JTextField tfCidade = new JTextField(cidade);
		dialog.add(tfCidade);

		dialog.add(new JLabel("UF:"));
		JTextField tfUf = new JTextField(uf);
		dialog.add(tfUf);

		dialog.add(new JLabel("CEP:"));
		JTextField tfCep = new JTextField(cep);
		dialog.add(tfCep);

		dialog.add(new JLabel("Estado Civil:"));
		JTextField tfEstadoCivil = new JTextField(estadoCivil);
		dialog.add(tfEstadoCivil);

		dialog.add(new JLabel("Telefone:"));
		JTextField tfTelefone = new JTextField(telefone);
		dialog.add(tfTelefone);

		dialog.add(new JLabel("RG:"));
		JTextField tfRg = new JTextField(rg);
		dialog.add(tfRg);

		dialog.add(new JLabel("Orgão Emissor:"));
		JTextField tfOrgaoEmissor = new JTextField(orgaoEmissor);
		dialog.add(tfOrgaoEmissor);

		dialog.add(new JLabel("Email:"));
		JTextField tfEmail = new JTextField(email);
		dialog.add(tfEmail);

		dialog.add(new JLabel("Nome da Mãe:"));
		JTextField tfNomeMae = new JTextField(nomeMae);
		dialog.add(tfNomeMae);

		dialog.add(new JLabel("Nome do Pai:"));
		JTextField tfNomePai = new JTextField(nomePai);
		dialog.add(tfNomePai);

		JButton btnSalvar = new JButton("Salvar");
		dialog.add(btnSalvar);

		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String novoNome = tfNome.getText().trim();
				String novaDataNascimento = tfDataNascimento.getText().trim();
				String novoSexo = cbSexo.getSelectedItem().toString();
				String novoCpf = tfCpf.getText().trim();
				String novoEndereco = tfEndereco.getText().trim();
				String novoNumero = tfNumero.getText().trim();
				String novoBairro = tfBairro.getText().trim();
				String novaCidade = tfCidade.getText().trim();
				String novoUf = tfUf.getText().trim();
				String novoCep = tfCep.getText().trim();
				String novoEstadoCivil = tfEstadoCivil.getText().trim();
				String novoTelefone = tfTelefone.getText().trim();
				String novoRg = tfRg.getText().trim();
				String novoOrgaoEmissor = tfOrgaoEmissor.getText().trim();
				String novoEmail = tfEmail.getText().trim();
				String novoNomeMae = tfNomeMae.getText().trim();
				String novoNomePai = tfNomePai.getText().trim();

				// Converter a data de nascimento para o formato aceito pelo MySQL
				SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatoMySQL = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date dataNascimento = formatoBrasileiro.parse(novaDataNascimento);
					novaDataNascimento = formatoMySQL.format(dataNascimento);
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(dialog, "Formato de data inválido. Use dd/MM/yyyy.");
					return;
				}

				// Atualizar o aluno no banco de dados
				try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
					String sql = "UPDATE alunos SET nome = ?, data_nascimento = ?, sexo = ?, cpf = ?, endereco = ?, "
							+ "numero = ?, bairro = ?, cidade = ?, uf = ?, cep = ?, estado_civil = ?, telefone = ?, "
							+ "rg = ?, orgao_emissor = ?, email = ?, nome_mae = ?, nome_pai = ? WHERE matricula = ?";
					try (PreparedStatement stmt = conn.prepareStatement(sql)) {
						stmt.setString(1, novoNome);
						stmt.setString(2, novaDataNascimento);
						stmt.setString(3, novoSexo);
						stmt.setString(4, novoCpf);
						stmt.setString(5, novoEndereco);
						stmt.setString(6, novoNumero);
						stmt.setString(7, novoBairro);
						stmt.setString(8, novaCidade);
						stmt.setString(9, novoUf);
						stmt.setString(10, novoCep);
						stmt.setString(11, novoEstadoCivil);
						stmt.setString(12, novoTelefone);
						stmt.setString(13, novoRg);
						stmt.setString(14, novoOrgaoEmissor);
						stmt.setString(15, novoEmail);
						stmt.setString(16, novoNomeMae);
						stmt.setString(17, novoNomePai);
						stmt.setString(18, matricula);
						stmt.executeUpdate();
						JOptionPane.showMessageDialog(dialog, "Aluno atualizado com sucesso.");
						dialog.dispose();
						carregarAlunos();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(dialog, "Erro ao atualizar aluno: " + ex.getMessage());
				}
			}
		});

		dialog.setVisible(true);
	}

	private void excluirAluno() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para excluir.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este aluno?", "Confirmação",
				JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		String matricula = tableModel.getValueAt(selectedRow, 0).toString();

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "DELETE FROM alunos WHERE matricula = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, matricula);
				stmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso.");
				carregarAlunos();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao excluir aluno: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GerenciarAlunos().setVisible(true);
			}
		});
	}
}
