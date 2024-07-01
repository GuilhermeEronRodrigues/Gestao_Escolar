package cadastro;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CadastroDeAlunos extends JFrame {
	private static final long serialVersionUID = 1L;
	public JTextField tfNome, tfEndereco, tfNumero, tfBairro, tfCidade, tfUf, tfCep, tfDataNascimento, tfEstadoCivil,
	tfTelefone, tfCpf, tfRg, tfOrgaoEmissor, tfEmail, tfNomeMae, tfNomePai;
	public JComboBox<String> cbSexo;
	private JButton btnSalvar, btnVoltar;

	private static final String URL = "jdbc:mysql://localhost:3306/cadastro_alunos";
	private static final String USER = "root";
	private static final String PASSWORD = "G1u2i3l4***";

	public CadastroDeAlunos(boolean isUpdateMode) {
		setTitle("Cadastro de Alunos");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("Nome:"), gbc);
		tfNome = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfNome, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(new JLabel("Endereço:"), gbc);
		tfEndereco = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfEndereco, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(new JLabel("Número:"), gbc);
		tfNumero = new JTextField(10);
		gbc.gridx = 1;
		panel.add(tfNumero, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(new JLabel("Bairro:"), gbc);
		tfBairro = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfBairro, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(new JLabel("Cidade:"), gbc);
		tfCidade = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfCidade, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(new JLabel("UF:"), gbc);
		tfUf = new JTextField(2);
		gbc.gridx = 1;
		panel.add(tfUf, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(new JLabel("CEP:"), gbc);
		tfCep = new JTextField(10);
		gbc.gridx = 1;
		panel.add(tfCep, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"), gbc);
		tfDataNascimento = new JTextField(10);
		gbc.gridx = 1;
		panel.add(tfDataNascimento, gbc);

		gbc.gridx = 0;
		gbc.gridy = 8;
		panel.add(new JLabel("Estado Civil:"), gbc);
		tfEstadoCivil = new JTextField(15);
		gbc.gridx = 1;
		panel.add(tfEstadoCivil, gbc);

		gbc.gridx = 0;
		gbc.gridy = 9;
		panel.add(new JLabel("Sexo:"), gbc);
		cbSexo = new JComboBox<>(new String[] { "M", "F" });
		gbc.gridx = 1;
		panel.add(cbSexo, gbc);

		gbc.gridx = 0;
		gbc.gridy = 10;
		panel.add(new JLabel("Telefone:"), gbc);
		tfTelefone = new JTextField(15);
		gbc.gridx = 1;
		panel.add(tfTelefone, gbc);

		gbc.gridx = 0;
		gbc.gridy = 11;
		panel.add(new JLabel("CPF:"), gbc);
		tfCpf = new JTextField(15);
		gbc.gridx = 1;
		panel.add(tfCpf, gbc);

		gbc.gridx = 0;
		gbc.gridy = 12;
		panel.add(new JLabel("RG:"), gbc);
		tfRg = new JTextField(15);
		gbc.gridx = 1;
		panel.add(tfRg, gbc);

		gbc.gridx = 0;
		gbc.gridy = 13;
		panel.add(new JLabel("Orgão Emissor:"), gbc);
		tfOrgaoEmissor = new JTextField(10);
		gbc.gridx = 1;
		panel.add(tfOrgaoEmissor, gbc);

		gbc.gridx = 0;
		gbc.gridy = 14;
		panel.add(new JLabel("Email:"), gbc);
		tfEmail = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfEmail, gbc);

		gbc.gridx = 0;
		gbc.gridy = 15;
		panel.add(new JLabel("Nome da Mãe:"), gbc);
		tfNomeMae = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfNomeMae, gbc);

		gbc.gridx = 0;
		gbc.gridy = 16;
		panel.add(new JLabel("Nome do Pai:"), gbc);
		tfNomePai = new JTextField(20);
		gbc.gridx = 1;
		panel.add(tfNomePai, gbc);

		JPanel buttonPanel = new JPanel();
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarAluno();
			}
		});
		buttonPanel.add(btnSalvar);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(btnVoltar);

		gbc.gridx = 0;
		gbc.gridy = 17;
		gbc.gridwidth = 2;
		panel.add(buttonPanel, gbc);

		add(panel);
	}

	private void salvarAluno() {
		String nome = tfNome.getText().trim();
		String endereco = tfEndereco.getText().trim();
		String numero = tfNumero.getText().trim();
		String bairro = tfBairro.getText().trim();
		String cidade = tfCidade.getText().trim();
		String uf = tfUf.getText().trim();
		String cep = tfCep.getText().trim();
		String dataNascimento = tfDataNascimento.getText().trim();
		String estadoCivil = tfEstadoCivil.getText().trim();
		String sexo = (String) cbSexo.getSelectedItem();
		String telefone = tfTelefone.getText().trim();
		String cpf = tfCpf.getText().trim();
		String rg = tfRg.getText().trim();
		String orgaoEmissor = tfOrgaoEmissor.getText().trim();
		String email = tfEmail.getText().trim();
		String nomeMae = tfNomeMae.getText().trim();
		String nomePai = tfNomePai.getText().trim();

		if (nome.isEmpty() || endereco.isEmpty() || numero.isEmpty() || bairro.isEmpty() || cidade.isEmpty()
				|| uf.isEmpty() || cep.isEmpty() || dataNascimento.isEmpty() || estadoCivil.isEmpty() || sexo.isEmpty()
				|| telefone.isEmpty() || cpf.isEmpty() || rg.isEmpty() || orgaoEmissor.isEmpty() || email.isEmpty()
				|| nomeMae.isEmpty() || nomePai.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
			return;
		}

		String matricula = gerarMatricula();
		String dataNascimentoMysql = converterDataParaMysql(dataNascimento);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "INSERT INTO alunos (matricula, nome, endereco, numero, bairro, cidade, uf, cep, data_nascimento, estado_civil, sexo, telefone, cpf, rg, orgao_emissor, email, nome_mae, nome_pai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, matricula);
				stmt.setString(2, nome);
				stmt.setString(3, endereco);
				stmt.setString(4, numero);
				stmt.setString(5, bairro);
				stmt.setString(6, cidade);
				stmt.setString(7, uf);
				stmt.setString(8, cep);
				stmt.setString(9, dataNascimentoMysql);
				stmt.setString(10, estadoCivil);
				stmt.setString(11, sexo);
				stmt.setString(12, telefone);
				stmt.setString(13, cpf);
				stmt.setString(14, rg);
				stmt.setString(15, orgaoEmissor);
				stmt.setString(16, email);
				stmt.setString(17, nomeMae);
				stmt.setString(18, nomePai);

				stmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso.");
				limparCampos();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao salvar aluno: " + e.getMessage());
		}
	}

	private String gerarMatricula() {
		Random random = new Random();
		int numero = random.nextInt(1000000);
		return String.format("%06d", numero);
	}

	private String converterDataParaMysql(String data) {
		SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatoMysql = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = formatoBrasileiro.parse(data);
			return formatoMysql.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void limparCampos() {

		tfNome.setText("");
		tfEndereco.setText("");
		tfNumero.setText("");
		tfBairro.setText("");
		tfCidade.setText("");
		tfUf.setText("");
		tfCep.setText("");
		tfDataNascimento.setText("");
		tfEstadoCivil.setText("");
		cbSexo.setSelectedIndex(0);
		tfTelefone.setText("");
		tfCpf.setText("");
		tfRg.setText("");
		tfOrgaoEmissor.setText("");
		tfEmail.setText("");
		tfNomeMae.setText("");
		tfNomePai.setText("");
	}

	public void preencherCampos(ResultSet rs) throws SQLException {
		tfNome.setText(rs.getString("nome"));
		tfEndereco.setText(rs.getString("endereco"));
		tfNumero.setText(rs.getString("numero"));
		tfBairro.setText(rs.getString("bairro"));
		tfCidade.setText(rs.getString("cidade"));
		tfUf.setText(rs.getString("uf"));
		tfCep.setText(rs.getString("cep"));

		// Convertendo a data para o formato correto
		String dataNascimentoStr = rs.getString("data_nascimento");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dataNascimento = sdf.parse(dataNascimentoStr);
			SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy");
			tfDataNascimento.setText(sdfOutput.format(dataNascimento));
		} catch (ParseException e) {
			e.printStackTrace();
			tfDataNascimento.setText("");
		}

		tfEstadoCivil.setText(rs.getString("estado_civil"));
		cbSexo.setSelectedItem(rs.getString("sexo"));
		tfTelefone.setText(rs.getString("celular"));
		tfCpf.setText(rs.getString("cpf"));
		tfRg.setText(rs.getString("rg"));
		tfOrgaoEmissor.setText(rs.getString("orgao_emissor"));
		tfEmail.setText(rs.getString("email"));
		tfNomeMae.setText(rs.getString("nome_mae"));
		tfNomePai.setText(rs.getString("nome_pai"));
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			CadastroDeAlunos frame = new CadastroDeAlunos(false);
			frame.setVisible(true);
		});
	}
}
