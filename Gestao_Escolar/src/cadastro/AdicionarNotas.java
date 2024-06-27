package cadastro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdicionarNotas extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField tfMatricula, tfCPF, tfNome, tfNota1, tfNota2, tfNota3, tfNota4, tfFrequencia;
	private JButton btnSalvar, btnCancelar;

	private static final String URL = "jdbc:mysql://localhost:3306/cadastro_alunos";
	private static final String USER = "root";
	private static final String PASSWORD = "G1u2i3l4***";

	public AdicionarNotas(JFrame parent, String[] dadosAluno) {
		super(parent, "Adicionar/Atualizar Notas", true);
		setLayout(new BorderLayout());
		setSize(400, 300);
		setLocationRelativeTo(parent);

		JPanel panel = new JPanel(new GridLayout(9, 2));

		panel.add(new JLabel("Matrícula:"));
		tfMatricula = new JTextField(dadosAluno != null ? dadosAluno[0] : "");
		tfMatricula.setEditable(false);
		panel.add(tfMatricula);

		panel.add(new JLabel("CPF:"));
		tfCPF = new JTextField(dadosAluno != null ? dadosAluno[1] : "");
		tfCPF.setEditable(false);
		panel.add(tfCPF);

		panel.add(new JLabel("Nome:"));
		tfNome = new JTextField(dadosAluno != null ? dadosAluno[2] : "");
		tfNome.setEditable(false);
		panel.add(tfNome);

		panel.add(new JLabel("Nota 1:"));
		tfNota1 = new JTextField(dadosAluno != null ? dadosAluno[3] : "");
		panel.add(tfNota1);

		panel.add(new JLabel("Nota 2:"));
		tfNota2 = new JTextField(dadosAluno != null ? dadosAluno[4] : "");
		panel.add(tfNota2);

		panel.add(new JLabel("Nota 3:"));
		tfNota3 = new JTextField(dadosAluno != null ? dadosAluno[5] : "");
		panel.add(tfNota3);

		panel.add(new JLabel("Nota 4:"));
		tfNota4 = new JTextField(dadosAluno != null ? dadosAluno[6] : "");
		panel.add(tfNota4);

		panel.add(new JLabel("Frequência:"));
		tfFrequencia = new JTextField(dadosAluno != null ? dadosAluno[7] : "");
		panel.add(tfFrequencia);

		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarNotas();
			}
		});
		buttonPanel.add(btnSalvar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(btnCancelar);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void salvarNotas() {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "INSERT INTO notas (matricula, nota1, nota2, nota3, nota4, frequencia, situacao) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE "
					+ "nota1 = VALUES(nota1), nota2 = VALUES(nota2), nota3 = VALUES(nota3), nota4 = VALUES(nota4), "
					+ "frequencia = VALUES(frequencia), situacao = VALUES(situacao)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, tfMatricula.getText());
				stmt.setString(2, tfNota1.getText());
				stmt.setString(3, tfNota2.getText());
				stmt.setString(4, tfNota3.getText());
				stmt.setString(5, tfNota4.getText());
				stmt.setString(6, tfFrequencia.getText());
				stmt.setString(7,
						calcularSituacao(Double.parseDouble(tfNota1.getText()), Double.parseDouble(tfNota2.getText()),
								Double.parseDouble(tfNota3.getText()), Double.parseDouble(tfNota4.getText()),
								Double.parseDouble(tfFrequencia.getText())));
				stmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "Notas salvas com sucesso!");
				dispose();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao salvar notas: " + e.getMessage());
		}
	}

	private String calcularSituacao(double nota1, double nota2, double nota3, double nota4, double frequencia) {
		double media = (nota1 + nota2 + nota3 + nota4) / 4.0;
		if (frequencia < 75.0) {
			return "Reprovado por Frequência";
		} else if (media < 6.0) {
			return "Reprovado por Nota";
		} else {
			return "Aprovado";
		}
	}
}
