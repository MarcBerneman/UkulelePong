package uart;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import game.Simon;

@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel implements ActionListener {
	Simon simon = new Simon(4, this);
	
	SerialPort port;
	PrintWriter output;
	JButton connectButton = new JButton("Connect");
	JTextField dutycycleField = new JTextField("0");
	JButton sendDutycycle = new JButton("Dutycyle");
	JButton newGame = new JButton("New Game");

	public ConnectionPanel() {
		JComboBox<String> portList = new JComboBox<String>();
		portList.setBounds(25, 25, 100, 25);
		add(portList);

		connectButton.setBounds(150, 25, 100, 25);
		add(connectButton);
		SerialPort[] portNames = SerialPort.getCommPorts();
		for (int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());

		dutycycleField.setBounds(150, 75 + 50, 100, 25);
		add(dutycycleField);
		sendDutycycle.setFont(new Font("Arial", Font.BOLD, 10));
		sendDutycycle.addActionListener(this);
		sendDutycycle.setBounds(275, 75 + 50 * 1, 50, 25);
		sendDutycycle.setEnabled(true);
		sendDutycycle.setFocusable(true);
		add(sendDutycycle);
		newGame.setFont(new Font("Arial", Font.BOLD, 10));
		newGame.addActionListener(this);
		newGame.setBounds(275, 75 + 50 * 1, 50, 25);
		newGame.setEnabled(true);
		newGame.setFocusable(true);
		add(newGame);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (connectButton.getText().equals("Connect")) {
					// probeer te verbinden met de seriele poort
					port = SerialPort.getCommPort(portList.getSelectedItem().toString());
					port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if (port.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
						output = new PrintWriter(port.getOutputStream());
					}
					port.addDataListener(new SerialPortDataListener() {
						@Override
						public int getListeningEvents() {
							return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
						}

						// stringbuilder aanmaken om chars samen te voegen tot
						// string
						StringBuilder sb = new StringBuilder();

						@Override
						public void serialEvent(SerialPortEvent event) {
							if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
								return;
							byte[] newData = new byte[port.bytesAvailable()];
							port.readBytes(newData, newData.length);
							for (int i = 0; i < newData.length; i++) {
								int data = newData[i] - 48; // convert from
															// ascii to int
								if (data != 14) {
									sb.append(data);
								} else {
									//System.out.println(sb.toString());
									sb.setLength(0);
								}
							}
						}

					});

				} else {
					// verbinding met seriële poort verbreken
					port.closePort();
					portList.setEnabled(true);
					connectButton.setText("Connect");

				}
			}
		});

	}

	public void sendData(String str) {
		if (connectButton.getText().equals("Disconnect")) {
			output.print(str);
			output.flush();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(sendDutycycle)) {
			sendData("D" + dutycycleField.getText());
			System.out.println("D" + dutycycleField.getText());
			//GameMain.dutycyle = Integer.parseInt(dutycycleField.getText());
		}
		else if(e.getSource().equals(newGame))
			try {
				simon.game();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	}
}
