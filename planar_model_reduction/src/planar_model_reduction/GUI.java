package planar_model_reduction;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import java.util.ArrayList; // import the ArrayList class

public class GUI implements ActionListener {

	// initlaize a list of some letters and there inverses to use for buttons
	Letter a = new Letter('a', false);
	Letter a1 = new Letter('a', true);

	Letter b = new Letter('b', false);
	Letter b1 = new Letter('b', true);

	Letter c = new Letter('c', false);
	Letter c1 = new Letter('c', true);

	Letter d = new Letter('d', false);
	Letter d1 = new Letter('d', true);

	Letter[] let_set = new Letter[] { a, a1, b, b1, c, c1, d, d1 };
	ArrayList<Integer> letter_index = new ArrayList<Integer>();

	// create buttons for letters
	JButton button1, button2, button3, button4, button5, button6, button7, button8;

	// button for getting solution or getting instructions
	JButton getSolution, instructions;
	// button for delete and clear
	JButton delete, clear;

	int count = 0;
	String display_text = "";
	
	JLabel label1;
	JButton button;
	JPanel panel;
	
	// create JTextField
	JTextField field = new JTextField();
	
	

	public GUI() {
		JFrame frame = new JFrame();

		button1 = new JButton(a.toString());
		button1.addActionListener(new ButtonListener());
		button2 = new JButton(a1.toString());
		button2.addActionListener(new ButtonListener());
		button3 = new JButton(b.toString());
		button3.addActionListener(new ButtonListener());
		button4 = new JButton(b1.toString());
		button4.addActionListener(new ButtonListener());
		button5 = new JButton(c.toString());
		button5.addActionListener(new ButtonListener());
		button6 = new JButton(c1.toString());
		button6.addActionListener(new ButtonListener());;
		button7 = new JButton(d.toString());
		button7.addActionListener(new ButtonListener());
		button8 = new JButton(d1.toString());
		button8.addActionListener(new ButtonListener());
		clear = new JButton("Clear");
		clear.addActionListener(new ButtonListener());
		delete = new JButton("Delete");
		delete.addActionListener(new ButtonListener());
		getSolution = new JButton("Get Solution");
		getSolution.addActionListener(new ButtonListener());
		label1 = new JLabel("Word: ");
		
		
		//instructions button
		instructions = new JButton("Help");
		instructions.addActionListener(new ButtonListener());


		// Sets the specified boolean to indicate whether or not
		// this textfield should be editable.
		field.setEditable(false);

		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		panel.add(button1, gbc);
		panel.add(button2, gbc);
		gbc.gridx++;
		panel.add(button3, gbc);
		panel.add(button4, gbc);
		gbc.gridx++;
		panel.add(button5, gbc);
		panel.add(button6, gbc);
		gbc.gridx++;
		panel.add(button7, gbc);
		panel.add(button8, gbc);
		gbc.gridx=0;
		panel.add(label1, gbc);
		gbc.gridx++;
		panel.add(field, gbc);
		gbc.gridx++;
		panel.add(delete, gbc);
		gbc.gridx= 0;
		panel.add(clear, gbc);
		gbc.gridx++;
		panel.add(getSolution, gbc);
		gbc.gridx++;
		panel.add(instructions, gbc);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Word Classification");
		frame.pack();
		frame.setVisible(true);

		
		
		
	}

	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource().hashCode() == clear.hashCode()) {
				display_text = "";
				letter_index.clear();
				field.setText(display_text);
				panel.repaint();
				panel.revalidate();
			}
			else if(e.getSource().hashCode() == delete.hashCode()) {
				if (letter_index.size() == 0)
					;
				else {
					int delete_pos = display_text.length()-1;
					if(let_set[letter_index.get(letter_index.size()-1)].inv == false)
						display_text = display_text.substring(0, delete_pos);
					else
						display_text = display_text.substring(0, delete_pos-2);
					letter_index.remove(letter_index.size()-1);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}

			}
			else if(e.getSource().hashCode() == getSolution.hashCode()) {
				if (letter_index.size() == 0)
					;
				else {
					Letter[] new_mod =new Letter[letter_index.size()];
					for(int i=0;i<letter_index.size();i++) {
						new_mod[i]=let_set[letter_index.get(i)];
					}
						Word new_word = new Word(new_mod);
						if(new_word.is_surface()) {
							Reduction_Object reduce_this = new Reduction_Object(new_word);
							reduce_this.get_normal_form();
							System.out.println("\n");
						}
						else {
							GUI.infoBox("That is not a surface." + "\n" + "A surface contains exact pairs of letters.", "Error");
						}
				}

				
				
			}
			else {
				int test1 = e.getSource().hashCode();
				if(test1 == button1.hashCode()) {
					
					display_text = display_text + let_set[0].toString();
					letter_index.add(0);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button2.hashCode()) {
					display_text = display_text + let_set[1].toString();
					letter_index.add(1);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button3.hashCode()) {
					display_text = display_text + let_set[2].toString();
					letter_index.add(2);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button4.hashCode()) {
					display_text = display_text + let_set[3].toString();
					letter_index.add(3);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button5.hashCode()) {
					display_text = display_text + let_set[4].toString();
					letter_index.add(4);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button6.hashCode()) {
					display_text = display_text + let_set[5].toString();
					letter_index.add(5);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button7.hashCode()) {
					display_text = display_text + let_set[6].toString();
					letter_index.add(6);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else if(test1 == button8.hashCode()) {
					display_text = display_text + let_set[7].toString();
					letter_index.add(7);
					field.setText(display_text);
					panel.repaint();
					panel.revalidate();
				}
				else {
					GUI.infoBox("A surface contains exact pairs of letters."+ "\n" + "You create a word by clicking the buttons."+ "\n" + "The solution is printed to the console.", "Help");
				}
				}
			}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(int i=0;i< let_set.length;i++) {
			if(let_set[i].toString() == e.getActionCommand()) {
				display_text = display_text + e.getActionCommand();
				field.setText(display_text);
			}
		}

	}
	
	
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}
