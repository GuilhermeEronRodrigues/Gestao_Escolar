package cadastro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main_GerenciarAlunos extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton btnCadastrar, btnGerenciar, btnNotas;

	public Main_GerenciarAlunos() {
		setTitle("Sistema de Cadastro de Alunos");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		btnCadastrar = new JButton("Cadastrar Alunos");
		btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 16));
		btnCadastrar.setPreferredSize(new Dimension(200, 40));
		btnCadastrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CadastroDeAlunos(rootPaneCheckingEnabled).setVisible(true);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(btnCadastrar, gbc);

		btnGerenciar = new JButton("Gerenciar Alunos");
		btnGerenciar.setFont(new Font("Arial", Font.PLAIN, 16));
		btnGerenciar.setPreferredSize(new Dimension(200, 40));
		btnGerenciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GerenciarAlunos().setVisible(true);
			}
		});
		gbc.gridy = 1;
		panel.add(btnGerenciar, gbc);

		btnNotas = new JButton("Gerenciar Notas");
		btnNotas.setFont(new Font("Arial", Font.PLAIN, 16));
		btnNotas.setPreferredSize(new Dimension(200, 40));
		btnNotas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GerenciarNotas().setVisible(true);
			}
		});
		gbc.gridy = 2;
		panel.add(btnNotas, gbc);

		add(panel);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main_GerenciarAlunos().setVisible(true);
			}
		});
	}
}
